package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import Server.IServer;
import Server.SocialServer;

public class SocialClient extends UnicastRemoteObject implements IClient{
	
	private static final long serialVersionUID = 1L;

	private String username = null;
	private String password = null;
	private String UID = null;
	private static Socket socket = null;
	private static String remoteAddress = "localhost";
	private static int remotePort = 52609;
	private static int listeningPort;
	private LinkedList<String> messages;
	private IServer server;
	
	protected SocialClient() throws RemoteException {
		super();
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
	 * fornite da SocialClient digitando un intero. Se l'intero digitato corrisponde
	 * all'operazione di logout questa procedura non viene richiamata.
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
			socket = new Socket(remoteAddress, remotePort);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
			switch(operation){
				case 1: 
					signIn(operation, in, reader, writer);
					break;
				case 2:
					login(operation, in, reader, writer);
					break;
				case 3:
					if(UID != null)
						friendRequest(operation, in, reader, writer);
					break;
				case 4:
					if(UID != null)
						showFriendRequest(operation, in, reader, writer);
					break;
				case 5:
					if(UID != null) 
						showFriendList(operation, in, reader, writer);
					break;
				case 6:
					if(UID != null)
						searchUser(operation, in, reader, writer);
					break;
				case 7:
					if(UID != null)
						publishContent(operation, in, reader, writer);
					break;
				case 8:
					if(UID != null)
						registerInterest(operation, in, reader, writer);
					break;
				case 9:
					if(UID != null)
						showContents();
					break;
				case 10:
					if(UID != null)
						logout(operation, writer);
					break;
				default:
					System.out.println("operazione non consentita");
					break;		
			}
		} catch (NumberFormatException e){
			System.out.println("error: inserire un intero. " + e.getMessage());
		}  catch(IOException e){
			System.out.println("error: errore di I/O. " + e.getMessage());
		} catch (NotBoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null) writer.close();
				if(reader != null) reader.close();
			} catch(IOException e){
				System.out.println("error: errore chiusura stream. " + e.getMessage());
			}
		}
	}

	/** permette all'utente di registrarsi inserendo username e password
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge l'input da tastiera
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 */
	private void signIn(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{	
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
			
		String response = reader.readLine();
		System.out.println(response);
	}
	
	/** permette all'utente già registrato di effettuare il login
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge l'input da tastiera
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 * @throws NotBoundException 
	 */
	private void login(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException, NotBoundException{
		System.out.println("username?");
		String usr = in.readLine();
		System.out.println("password?");
		String psw = in.readLine();
		
		writer.write(operation);
		writer.write(usr);
		writer.newLine();
		writer.write(psw);
		writer.newLine();
		writer.flush();
		
		// riceve il token UID e la porta per ascoltare connessioni dal server
		String msg = reader.readLine();
		if(msg.equals("NO")){
			System.out.println("error: login failed");
		} else {
			UID = msg;
			listeningPort = reader.read();
			// attivo il thread per ricevere connessioni
			Thread t = new Thread(new TaskConnection(new ServerSocket(listeningPort)));
			t.setDaemon(true);
			t.start();
			// attivo il thread per ricevere i keepAlive
			Thread t2 = new Thread(new KeepAlive(username));
			t2.setDaemon(true);
			t2.start();
			
			// registro la callback
			server = (IServer) LocateRegistry.getRegistry(Registry.REGISTRY_PORT).lookup(SocialServer.SERVICE_NAME);
			server.registerForCallback(username, this);
		}
	}
	
	/** permette all'utente di effettuare una richiesta di amicizia
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge l'input da tastiera
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 */
	private void friendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{		
		System.out.println("insert a username");
		String friend = in.readLine();
					
		writer.write(operation);
		// se il token non è valido...
		if(!sendToken(reader, writer)){
			// ALTERNATIVA È SOLLEVARE UN'ECCEZIONE
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {	
			writer.write(friend);
			writer.newLine();
			writer.flush();
			
			String response = reader.readLine();
			System.out.println(response);
		}		
	}
	
	/** permette all'utente di visualizzare le richieste di amicizia pendenti e scegliere se accettare/rifiutare una di queste.
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge l'input da tastiera
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 */
	private void showFriendRequest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{			
			writer.write(operation);
			// se il token non è valido
			if(!sendToken(reader, writer)){
				System.out.println("token invalidato/sessione finita, rieffettua il login");
			} else {
				System.out.println("le richieste di amicizia pendenti sono:");
				String line;
				while(!(line = reader.readLine()).equals("END"))
					System.out.println(line);
				
				System.out.println("scegli un nome");
				String userB = in.readLine();
				System.out.println("SI - accetta\nNO - rifiuta");
				String choice = in.readLine();
				
				writer.write(userB);
				writer.newLine();
				writer.write(choice);
				writer.newLine();
				writer.flush();
				
				String response = reader.readLine();
				System.out.println(response);
			}
	}
	
	/** permette all'utente di visualizzare la lista degli amici e sapere, per ognuno di essi, se sono online/offline.
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge l'input da tastiera
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 */
	private void showFriendList(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{
		writer.write(operation);
		// se il token non è valido...
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else { 
			String line;
			System.out.println("la tua lista di amici è:");
			while(!(line = reader.readLine()).equals("END")){
				System.out.println(line);
			}
		}
	}
	
	/**
	 * permette di ricercare utenti inserendo il nome
	 * @param operation, operazione scelta dall'utente
	 * @param in, legge dallo stardard input
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException 
	 */
	private void searchUser(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException{
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
			String line;
			while(!(line = reader.readLine()).equals("END")){
				System.out.println(line);
			}			
		}
	}	
		
	private void publishContent(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException {
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
	
	private void registerInterest(int operation, BufferedReader in, BufferedReader reader, BufferedWriter writer) throws IOException {
		writer.write(operation);
		if(!sendToken(reader, writer)){
			System.out.println("token invalidato/sessione finita, rieffettua il login");
		} else {
			String toFollow = in.readLine();
			server.follow(username, toFollow);
		}
	}	
	
	/** permette all'utente di effettuare il logout
	 * 
	 * @param operation, operazione scelta dall'utente
	 * @param writer, scrive sullo stream della socket
	 * @throws IOException
	 */
	private void logout(int operation, BufferedWriter writer) throws IOException{
		writer.write(operation);
		writer.write(username);
		writer.newLine();
		writer.flush();
	}
	
	/** invia il token al server ad ogni inizio attività.
	 * 
	 * @param reader, legge dallo stream della socket
	 * @param writer, scrive sullo stream della socket
	 * @return true se il token è valido, false altrimenti
	 * @throws IOException
	 */
	private boolean sendToken(BufferedReader reader, BufferedWriter writer) throws IOException{
		writer.write(username);
		writer.newLine();
		writer.write(UID);
		writer.newLine();
		writer.flush();
		String msg = reader.readLine();
		boolean result = Boolean.parseBoolean(msg);
		return result;
	}
	
	/**
	 * stampa le opzioni che può scegliere l'utente.
	 */
	private void printMenu(){
		System.out.println("(1) - sign in");
		System.out.println("(2) - login");
		if(UID != null){
			System.out.println("(3) - submit a friend request");
			System.out.println("(4) - show your friend requests");
			System.out.println("(5) - show your friend list");
			System.out.println("(6) - search a user");
			System.out.println("(7) - publish content");
			System.out.println("(8) - register interest");
			System.out.println("(9) - show contents");
			System.out.println("(10) - logout");
		}
	}


	@Override
	public void notifyContent(String msg) throws RemoteException {
		if(messages == null)
			messages = new LinkedList<String>();
		messages.add(msg);
	}
	
	private void showContents() {
		while(messages != null){
			System.out.println(messages.removeFirst());
		}
			
	}

}
