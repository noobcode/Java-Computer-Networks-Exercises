package simpleSocial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class SocialClient extends UnicastRemoteObject implements IClient, ClientInterface{
	
	private static final long serialVersionUID = 1L;

	private String username = null;
	private String password = null;
	private UUID   uid 		= null;
	private Socket socket 	= null;
	private int listeningPort;					// porta del Welcoming Socket del Client
	private LinkedList<String> messages;		// lista dei messaggi generati dagli utenti che si sta seguendo
	private LinkedList<String> friendRequests;	// lista richieste di amicizia
	private IServer server;						// stub
	private RequestHandler rh;					// thread che si occupa delle richieste di amicizia
	private KeepAlive ka;						// thread che risponde ai messaggi di keepAlive
	private static final String REMOTE_ADDRESS 	= "localhost";	// indirizzo remoto del server
	private static final int 	REMOTE_PORT 	= 52609;		// porta remota del Welcoming Socket
	
	
	public SocialClient() throws RemoteException {
		super();
		messages	   = new LinkedList<String>();	
		friendRequests = new LinkedList<String>();
	}
	
	public static void main(String[] args) {
		SocialClient client = null;
		try {
			client = new SocialClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			client.initialWindow(in);
		}
	}
	
	/**
	 * finestra iniziale invocata continuamente nel main(), permette all'utente di scegliere tra le funzionalità
	 * fornite da SocialClient digitando un intero.
	 * @param in
	 * @return
	 */
	private void initialWindow(BufferedReader in){
		printMenu();
		BufferedReader reader = null;
		BufferedWriter writer = null;
		int operation = 0;
		
		try {
			operation = Integer.parseInt(in.readLine());
			socket = new Socket(REMOTE_ADDRESS, REMOTE_PORT);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
			switch(operation){
				case 1: 
					signIn(operation, in, reader, writer);
					break;
				case 2:
					login(operation, in, reader, writer);
					if(username != null){
						// ricarica messaggi ricevuti ma non ancora visualizzati
						loadFromFile(messages, username + ".messages");
						//friendRequests = new LinkedList<String>();
						loadFromFile(friendRequests, username + ".requests");
					}
					break;
				case 3:
					if(uid != null)
						friendRequest(operation, in, reader, writer);
					break;
				case 4:
					if(uid != null)
						showFriendRequest(operation, in, reader, writer);
					break;
				case 5:
					if(uid != null) 
						showFriendList(operation, reader, writer);
					break;
				case 6:
					if(uid != null)
						searchUser(operation, in, reader, writer);
					break;
				case 7:
					if(uid != null)
						publishContent(operation, in, reader, writer);
					break;
				case 8:
					if(uid != null)
						registerInterest(operation, in, reader, writer);
					break;
				case 9:
					if(uid != null)
						showContents();
					break;
				case 10:
					if(uid != null)
						logout(operation, writer);
					break;
				case 11:
					if(uid != null)
						refurbishToken(operation, reader, writer);
					break;
				default:
					System.out.println("operazione non consentita");
					break;		
			}
		} catch (NumberFormatException e){
			System.out.println("error: inserire un intero. " + e.getMessage());
		} catch(ConnectException e){
			System.out.println("error: connessione al server fallita." + e.getMessage());
			if(username != null)
				System.out.println("riavviare l'applicazione");
		} catch(IOException e){
			System.out.println("error: errore di I/O. " + e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("errore durante il logout. " + e.getMessage());
		} finally {
			try {
				if(writer != null) writer.close();
				if(reader != null) reader.close();
			} catch(IOException e){
				System.out.println("error: errore chiusura stream. " + e.getMessage());
			}
		}
	}

	public void signIn(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{	
		System.out.println("username?");
		String user = in.readLine();
		System.out.println("password?");
		String psw = in.readLine();
			
		writer.write(operation);
		writer.write(user);
		writer.newLine();
		writer.write(psw);
		writer.newLine();
		writer.flush();
			
		String response = reader.readLine();
		System.out.println(response);
	}
	
	public void login(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException, NotBoundException{
		System.out.println("username?");
		username = in.readLine();
		System.out.println("password?");
		password = in.readLine();
		
		writer.write(operation);
		writer.write(username);
		writer.newLine();
		writer.write(password);
		writer.newLine();
		writer.flush();
		
		String msg = reader.readLine();
		if(msg.equals("NO")){
			System.out.println("error: login failed");
			username = password = null;
		} else {
			// riceve il token UID e la porta per ascoltare connessioni dal server
			uid 		  = UUID.fromString(msg);
			listeningPort = reader.read();
			
			// registro la callback
			server = (IServer) LocateRegistry.getRegistry(Registry.REGISTRY_PORT).lookup(SocialServer.SERVICE_NAME);
			server.registerForCallback(username, this);
			
			// attivo il thread per ricevere connessioni (verifica se l'utente è online, per le richieste di amicizia)
			rh = new RequestHandler(username, new ServerSocket(listeningPort), friendRequests);
			rh.start();
			
			// attivo il thread per ricevere i messaggi di keepAlive
			ka = new KeepAlive(username);
			ka.start();
		}
	}
	
	public void friendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{							
		writer.write(operation);
		// se il token non è valido...
		if(!sendToken(reader, writer)){
			// L' ALTERNATIVA È SOLLEVARE UN'ECCEZIONE
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			System.out.println("insert a username");
			String friend = in.readLine();
			writer.write(friend);
			writer.newLine();
			writer.flush();
			
			String response = reader.readLine();
			System.out.println(response);
		}		
	}
	
	public void showFriendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{
		if(friendRequests.isEmpty()){
			System.out.println("non ci sono richieste di amicizia");
			return;
		}
		
		writer.write(operation);
		// se il token non è valido
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			Iterator<String> i;
			i = friendRequests.iterator();
			System.out.println("le richieste di amicizia pendenti sono:");
			while(i.hasNext())
				System.out.println(i.next());
				
			System.out.println("scegli un nome");
			String userB = in.readLine();
			System.out.println("SI - accetta\nNO - rifiuta");
			String choice = in.readLine();
				
			// invia la scelta al server
			writer.write(userB);
			writer.newLine();
			writer.write(choice);
			writer.newLine();
			writer.flush();
				
			String response = reader.readLine();
			System.out.println(response);
				
			if(!response.equals("richiesta di amicizia inesistente")){
				// rimuove l'utente dalla lista e riscrive il file
				synchronized(friendRequests){
					friendRequests.remove(userB);
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(username + ".requests"));
				i = friendRequests.iterator();
				while(i.hasNext()){
					bw.write(i.next());
					bw.newLine();
				}
				bw.flush();
				bw.close();
			}
			
		}
	}
	
	public void showFriendList(int operation, BufferedReader reader, BufferedWriter writer) throws IOException{
		writer.write(operation);
		// se il token non è valido...
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else { 
			System.out.println("la tua lista di amici è:");
			String line = reader.readLine();
			while(!line.equals("END")){
				System.out.println(line);
				line = reader.readLine();
			}
		}
	}
	
	public void searchUser(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{
		writer.write(operation);
		if(!sendToken(reader,writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			System.out.println("inserire un nome da ricercare");
			String toFind = in.readLine();
			writer.write(toFind);
			writer.newLine();
			writer.flush();
			
			System.out.println("risultato della ricerca:");
			String line = reader.readLine();
			while(!line.equals("END")){
				System.out.println(line);
				line = reader.readLine();
			}			
		}
	}	
	
	public void publishContent(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException {
		writer.write(operation);
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			System.out.println("inserisci un messaggio da pubblicare");
			String toPublish = in.readLine();
			writer.write(toPublish);
			writer.newLine();
			writer.flush();
		}
	}	

	public void registerInterest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException {
		writer.write(operation);
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			System.out.println("inserire un nome da seguire");
			String toFollow = in.readLine();
			server.follow(username, toFollow);
		}
	}	
	
	public void logout(int operation, BufferedWriter writer) throws IOException, InterruptedException{
		writer.write(operation);
		writer.write(username);
		writer.newLine();
		writer.flush();
		uid = null;
		System.out.println("attendere...");
		rh.join();
		ka.join();
	}

	@Override
	public void notifyContent(String msg) throws RemoteException {
		synchronized(messages){
			messages.add(msg);
		}
		System.out.println("hai ricevuto un messaggio");
		
		// salva sul file i messaggi ricevuti ma non ancora letti
		BufferedWriter bf = null;
		try {
			bf = new BufferedWriter(new FileWriter(username + ".messages", true));
			bf.append(msg);
			bf.newLine();
			bf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
	}
	
	public void showContents() throws FileNotFoundException {
		if(messages.isEmpty()){
			System.out.println("non hai ricevuto messaggi\n");
			return;
		}
		
		System.out.println("i contenuti ricevuti sono:");
		while(!messages.isEmpty()){
			String msg = messages.removeFirst();
			System.out.println(msg);
		}
		
		// cancella il contenuto del file
		new PrintWriter(username + ".messages").close();			
	}
	
	@Override
	public void refurbishToken(int operation,BufferedReader reader, BufferedWriter writer) throws IOException {
		writer.write(operation);
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			uid = UUID.fromString(reader.readLine());
			System.out.println("token rinnovato");
		}
		
		
	}
	
	/** invia il token al server ad ogni inizio attività.
	 * 
	 * @param reader, 	legge dallo stream della socket
	 * @param writer, 	scrive sullo stream della socket
	 * @return true se il token è valido, false altrimenti
	 * @throws IOException
	 */
	private boolean sendToken(BufferedReader reader, BufferedWriter writer) throws IOException{
		writer.write(uid.toString());
		writer.newLine();
		writer.write(username);
		writer.newLine();
		writer.flush();
		String msg 	   = reader.readLine();
		boolean result = Boolean.parseBoolean(msg);
		return result;
	}
	
	/**
	 * stampa le opzioni che può scegliere l'utente.
	 * se l'uid è null significa che è stato fatto logout oppure non è mai stato fatto il login,
	 * quindi si mostrano solo le opzionii di registrazione e login.
	 * il caso in cui l'utente inserisca un valore non consentito è gestito esternamente.
	 */
	private void printMenu(){
		System.out.println("(1) - sign in");
		System.out.println("(2) - login");
		if(uid != null){
			System.out.println("(3) - submit a friend request");
			System.out.println("(4) - show your friend requests");
			System.out.println("(5) - show your friend list");
			System.out.println("(6) - search a user");
			System.out.println("(7) - publish content");
			System.out.println("(8) - register interest");
			System.out.println("(9) - show contents");
			System.out.println("(10) - logout");
			System.out.println("(11) - refurbish token");
		}
	}
	
	/**
	 * carica dal file 'filename' una lista e la salva in 'l'.
	 * la lista può corrispondere alle richieste di amicizia non ancora riscontrate, oppure ai
	 * messaggi generati dagli amici seguiti non ancora letti.
	 * @param l,		lista i cui elementi conterranno le righe sccritte sul file
	 * @param filename, file salvato sul disco
	 * @throws IOException
	 */
	private void loadFromFile(LinkedList<String> l, String filename) throws IOException{		
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println(filename + " non trovato. " + e.getMessage());
			return;
		}
		
		String line;
		while((line = r.readLine()) != null)
			l.add(line);
	
		r.close();
	}

}
