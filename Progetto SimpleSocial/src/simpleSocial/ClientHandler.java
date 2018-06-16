package simpleSocial;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import simpleSocial.User.Token;

public class ClientHandler implements Runnable{
	private SocialServer server;					// oggetto che rappresenta il Server, serve per la serializzazione
	private Socket client;							// Connection Socket associata al Client
	private Hashtable <String, User> registered;	// riferimento alla tabella degli utenti registrati
	private Hashtable <UUID, User> online;			// riferimento alla tabella degli utenti online
	private boolean[] clientPorts;					// riferimento alle porte disponibili per il client
	private String GROUP;
	private int PORT_GROUP;
	
	public ClientHandler(SocialServer s, Socket c, Hashtable<String, User> r, boolean[] cp, Hashtable<UUID, User> on, String g, int p){
		server 		= s;
		client 		= c;
		registered 	= r;
		clientPorts = cp;
		online 		= on;
		GROUP 		= g;
		PORT_GROUP 	= p;
	}
	
	public void run(){
		BufferedReader reader = null;
		BufferedWriter writer = null;
		int operation = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		
			operation = reader.read();
			switch(operation){
				case 1: 
					handleSignIn(reader, writer);
					break;
				case 2:
					handleLogin(reader, writer);
					break;
				case 3:
					handleFriendRequest(reader, writer);
					break;	
				case 4:
					handleViewFriendRequest(reader, writer);
					break;
				case 5:
					handleShowFriendList(reader, writer);
					break;
				case 6:
					handleSearchUser(reader,writer);
					break;
				case 7:
					handlePublishContent(reader, writer);
					break;
				case 8:
					checkToken(reader,writer);
					break;
				case 10:
					handleLogout(reader, writer);
					break;
				case 11:
					handleRefurbishToken(reader, writer);
			}
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) reader.close();
				if(writer != null) writer.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private void handleSignIn(BufferedReader reader, BufferedWriter writer) throws IOException{
		String username = reader.readLine();
		String password = reader.readLine();
		
		String msg;
		// se l'utente è già registrato
		if(registered.containsKey(username)){
			msg = "error: user already signed in";
		} else { // altrimenti, lo registro
			synchronized(registered){
				registered.put(username, new User(username, password));
			}
			msg = "user registered with success";
		}
		writer.write(msg);
		writer.newLine();
		writer.flush();
		
		// serializza
		Serializer s = server.getSerilizer();
		s.serialize();
		synchronized(s){
			s.notify();
		}
	}
	
	private void handleLogin(BufferedReader reader, BufferedWriter writer) throws IOException{
		String firstMsg = null;
		int secondMsg 	= -1;
		User u 			= null;
		String user 	= reader.readLine();
		String psw 		= reader.readLine();
		
		// se l'utente è registrato
		if(registered.containsKey(user)){
			u = registered.get(user);
			// se username e password corrispondono a quelli registrati
			if(u.getUsername().equals(user) && u.getPassword().equals(psw)){
				// genero il token
				UUID uid = UUID.randomUUID();
				// trovo una porta per accettare connessioni
				int listeningPort = findListeningPort();
				synchronized(u){
					u.setUID(uid, System.currentTimeMillis());
					u.setListeningPort(listeningPort);
					u.setLastKeepAlive(System.currentTimeMillis());
				}
				firstMsg  = u.getUID().getKey().toString();
				secondMsg = listeningPort;
				// l'utente viene inserito nella lista degli utenti online
				synchronized(online){
					online.put(uid, u);
				}
			} else {
				// se username e password non corrispondono
				firstMsg = "NO";
			}
		} else {
			// se l'utente non è registrato
			firstMsg = "NO";
		}
		writer.write(firstMsg);
		writer.newLine();
		if(!firstMsg.equals("NO")){
			writer.write(secondMsg);
			writer.newLine();
		}
		writer.flush();
		
	}

	private void handleFriendRequest(BufferedReader reader, BufferedWriter writer) throws IOException {
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		String userB  = reader.readLine();
		String msgToA = null;		
		
		// se provi a inviarti una richiesta di amicizia da solo...
		if(userA.equals(userB)){
			msgToA = "error: you can't send a friend request to yourself.";
		} else if(registered.containsKey(userB)) {
			// se l'utente B è registrato
			if(isOnline(userA, userB)) {
				// se l'utente B è online
				User A = registered.get(userA);
				User B = registered.get(userB);
				
				addRequests(A,B);
				msgToA = "friend request submitted";
			} else {
				// se l'utente B non è online
				msgToA = "user " + userB + " is not online at the moment. please try again later.";
			}
		} else {
			// se l'utente B non è registrato
			msgToA = "error: user " + userB + " does not exist";
		}
		writer.write(msgToA);
		writer.newLine();
		writer.flush();
	}
	
	/**
	 * controlla se l'userB è online, in caso affermativo gli invia un messaggio
	 * @param userA,	utente che manda la richiesta di amicizia
	 * @param userB,	utente che riceve la richiesta di amicizia
	 * @return true se l'utente B è online, false altrimenti
	 * @throws IOException
	 */
	private boolean isOnline(String userA, String userB) throws IOException{
		Socket 	s 		= null;
		User 	u   	= registered.get(userB);
		boolean result 	= false;
		int 	port 	= u.getListeningPort();
		
		// prova a stabilire una connessione TCP con l'utente B, se non ci riesce restituisce false
		try {
			s = new Socket("localhost", port);
		} catch(ConnectException e){
			return result;
		}
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		int requestCount   = u.getInputRequest().size();
		out.write("you have " + (requestCount+1) + " friend request."); // messaggio inviato a B
		out.newLine();
		
		// invia all'utente B il nome dell'User che cha fatto la richiesta (A) in modo tale che possa salvarlo in una sua lista interna
		out.write(userA);
		out.newLine();
		out.flush();
		
		if(out != null) out.close();
		if(s != null)   s.close();
		result = true;
		
		return result;	
	}
	
	/**
	 * aggiunge l'utente A nella lista delle richieste di amicizia dell'utente B.
	 * alla richiesta di amicizia è associato un parametro 'time' che rappresenta il
	 * momento in cui è stata fatta la richiesta.
	 * @param A,	utente che ha fatto la richiesta di amicizia
	 * @param B,	utente che ha ricevuto la richiesta di amicizia
	 */
	private void addRequests(User A, User B){
		Hashtable<String, Long> inputRequestB = B.getInputRequest();
		long time 	 = System.currentTimeMillis();
		String userA = A.getUsername();
		try {
			if(!inputRequestB.containsKey(userA)){
				synchronized(inputRequestB){
					inputRequestB.put(userA, time);
				}
			}
		} catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	private void handleViewFriendRequest(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		User A = registered.get(userA);
		Hashtable<String,Long> inputRequestA = A.getInputRequest();
		
		// se non ci sono richieste di amicizia
		if(inputRequestA.isEmpty())
			return;
		
		
		String userB  = reader.readLine();	// nome scelto dall'utente
		String choice = reader.readLine();	// scelta, se SI accetta
		String response;
		
		// se sono gia amici
		if(A.getFriends().contains(registered.get(userB)))
			response = "siete gia amici";
		// se la richiesta di amicizia esiste
		else if(inputRequestA.containsKey(userB)){
			if(choice.equals("SI")){
				// se accetta
				User B = registered.get(userB);	
				addFriend(A, B);
				addFriend(B, A);
				deleteRequest(A,B);
				response = "amicizia confermata";
			} else {
				// se non accetta
				response = "amicizia negata";
			}
		} else {
			// se la richiesta non esiste
			response = "richiesta di amicizia inesistente";
		}
		writer.write(response);
		writer.newLine();
		writer.flush();
		
		
		// serializza
		if(choice.equals("SI")){
			Serializer s = server.getSerilizer();
			s.serialize();
			synchronized(s){
				s.notify();
			}
		}
			
	}
	
	private void handleShowFriendList(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		User A = registered.get(userA);
		ArrayList<User> friendA = A.getFriends();
		// scorre la lista degli amici dell'utente A
		for(int i = 0; i < friendA.size(); i++){
			User u = friendA.get(i);
			String msg = u.getUsername() + " - " + (u.isOnline()?"online":"offline"); 
			writer.write(msg);
			writer.newLine();
		}
		writer.write("END");
		writer.newLine();
		writer.flush();
	}
	
	private void handleSearchUser(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		String toFind = reader.readLine();
		Enumeration<String> users = registered.keys();
		// scorro la lista dei registrati
		while(users.hasMoreElements()){
			String name = users.nextElement();
			if(name.contains(toFind)){
				writer.write(name);
				writer.newLine();
			}
		}
		writer.write("END");
		writer.newLine();
		writer.flush();
	}
	
	private void handlePublishContent(BufferedReader reader, BufferedWriter writer) throws IOException {
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		String toPublish = reader.readLine();
		toPublish 		 = userA + ": " + toPublish;
		ArrayList<User> followers = registered.get(userA).getFollowers();
		Iterator<User> i = followers.iterator();
		
		while(i.hasNext()){
			User u 	  = i.next();
			IClient c = u.getCallbackObj();
			// se l'utente è online lo notifica subito
			if(c != null){
				c.notifyContent(toPublish);
			} else { 
				// altrimenti il server salva il messaggio in una lista interna dell'utente
				LinkedList<String> messagesToSend = u.getMessagesToSend();
				synchronized(messagesToSend){
					messagesToSend.add(toPublish);
				}
			}
		}
	}
	
	private void handleLogout(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = reader.readLine();
		User A 		 = registered.get(userA);
		int port 	 = A.getListeningPort();
		
		synchronized(clientPorts){
			clientPorts[port] = false;
		}
		
		terminateClientThreads(A);
		
		A.setUID(null, 0);
		A.setCallbackObj(null);
	}
	
	private void handleRefurbishToken(BufferedReader reader, BufferedWriter writer) throws IOException {
		String userA = checkToken(reader, writer);
		if(userA == null)
			return;
		
		User A 		  = registered.get(userA);
		Token tokenA  = A.getUID();
		UUID newToken = UUID.randomUUID();
		
		// imposto il nuovo token all'utente
		synchronized(tokenA){
			tokenA.setKey(newToken);
			tokenA.setTime(System.currentTimeMillis());
		}
		
		// aggiorna la mappa <UUID -> User>
		synchronized(online){
			online.remove(tokenA);
			online.put(newToken, A);
		}
		
		writer.write(newToken.toString());
		writer.newLine();
		writer.flush();
	}
	
	private void terminateClientThreads(User a) throws IOException {
		// termina il thread taskConnection del client
		Socket s 		   = new Socket("localhost", a.getListeningPort());
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		out.write("END");
		out.newLine();
		out.flush();
				
		// termina il thread KeepAlive del client
		MulticastSocket s2 = new MulticastSocket();
		byte[] buffer 	   = new byte[32];
		buffer 			   = a.getUsername().getBytes();
		DatagramPacket dp  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(GROUP), PORT_GROUP);
		s2.send(dp);
				
		if(out != null) out.close();
		if(s != null) 	s.close();
		if(s2 != null) 	s2.close();
	}

	/**
	 * cerca una porta da assegnare al Welcoming Socket del client.
	 * @return una porta disponibile, -1 altrimenti
	 */
	private synchronized int findListeningPort(){
		int i;
		for(i = 1024; i < clientPorts.length; i++){
			if(clientPorts[i] == false){
				clientPorts[i] = true;
				break;
			}
		}
		return (i >= clientPorts.length? -1 : i);
	}
	
	/**
	 * da diventare l'utente A e B amici
	 * @param a,	utente
	 * @param b,	utente
	 */
	private void addFriend(User a, User b){
		ArrayList<User> listA = a.getFriends();
		synchronized(listA){
			listA.add(b);
		}
	}
	
	private void deleteRequest(User a, User b){
		Hashtable<String,Long> request = a.getInputRequest();
		synchronized(request){
			request.remove(b.getUsername());
		}
	}
	
	/**
	 * legge un uuid e controlla che sia valido
	 * @param reader, 	per leggere dallo stream della socket
	 * @param writer, 	per scrivere sullo stream della socket
	 * @return username associato all'uuid in caso di successo, null altrimenti
	 * @throws IOException
	 */
	private String checkToken(BufferedReader reader, BufferedWriter writer) throws IOException{
		UUID key 		= UUID.fromString(reader.readLine());	// uid letto dal client
		String username = reader.readLine();					// username dell'utente
		Token uid 		= registered.get(username).getUID();	// token assegnato all'utente lato server
		
		// controlla se 'key' è valido
		User u = registered.get(username);
		boolean response = online.get(key).equals(u);
		response = response && uid.isValid();
		response = response && uid.getKey().equals(key);
		writer.write(new String(""+response));
		writer.newLine();
		writer.flush();
		return (response? username: null);
	}	
}