package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import Client.IClient;
import Server.User.Token;

public class Task implements Runnable{

	private Socket client;
	private Hashtable<String, User> registered;	
	private boolean[] clientPorts;
	Random rand;
	
	public Task(Socket c, Hashtable<String, User> r, boolean[] cp, Random random){
		client = c;
		registered = r;
		clientPorts = cp;
		rand = random;
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
				case 10:
					handleLogout(reader);
					break;
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
		if(registered.containsKey(username)){
			msg = "error: user already signed in";
		} else {
			synchronized(registered){
				registered.put(username, new User(username, password));
			}
			msg = "user registered with success";
		}
		
		writer.write(msg);
		writer.newLine();
		writer.flush();
	}
	
	private void handleLogin(BufferedReader reader, BufferedWriter writer) throws IOException{
		String user = reader.readLine();
		String psw = reader.readLine();
		
		String firstMsg = null;
		int secondMsg = -1;
		// se l'utente è registrato
		if(registered.containsKey(user)){
			User u = registered.get(user);
			// se username e password corrispondono a quelli registrati
			if(u.getUsername().equals(user) && u.getPassword().equals(psw)){
				u.setOnline(true);
				u.setUID(user, System.currentTimeMillis()); // TOKEN? usa oggetto UUID
				u.setClient(client);
				int listeningPort = findListeningPort();
				u.setListeningPort(listeningPort);
				firstMsg = u.getUID().getKey();
				secondMsg = listeningPort;
			} else {
				firstMsg = "NO";
			}
		} else {
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
		String userA = reader.readLine();
		if(!checkToken(userA, reader, writer)){
			return;
		}
		
		String userB = reader.readLine();
		String msgToA = "";		
		
		if(userA.equals(userB)){
			msgToA = "error: you can't send a friend request to yourself.";
		} else if(registered.containsKey(userB)) {
			if(isOnline(userB)) {
				User A = registered.get(userA);
				User B = registered.get(userB);
				
				addRequests(A,B);
				msgToA = "friend request submitted";
			} else {
				msgToA = "error: user " + userB + " is not online at the moment. please try again later.";
			}
		} else {
			msgToA = "error: user " + userB + " does not exist";
		}
		writer.write(msgToA);
		writer.newLine();
		writer.flush();
	}
	
	private boolean isOnline(String name) throws IOException{
		User u = registered.get(name);
		Socket s = null;
		BufferedWriter out = null;
		boolean result = false;
		
		s = new Socket("localhost", u.getListeningPort());
		out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		int requestCount = u.getInputRequest().size();
		out.write("you have " + (requestCount+1) + " friend request."); // messaggio inviato a B
		out.newLine();
		out.flush();
		
		s.close();
		out.close();
		result = true;
		
		return result;	
	}
	
	public void addRequests(User A, User B){
		// ALTERNATIVA È THROWS NULLPOINTEXCEPTION
		if(A == null || B == null){
			System.out.println("A or B is null");
			return;
		}
		Hashtable<String, Long> hashA = A.getOutputRequest();
		Hashtable<String, Long> hashB = B.getInputRequest();
		long time = System.currentTimeMillis();
		String userA = A.getUsername();
		String userB = B.getUsername();
		
		try {
			if(!hashA.containsKey(userB)){
				synchronized(hashA){
					hashA.put(userB, time);
				}
			}
			if(!hashB.containsKey(userA)){
				synchronized(hashB){
					hashB.put(userA, time);
				}
			}
		} catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	private void handleViewFriendRequest(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = reader.readLine();
		if(!checkToken(userA, reader, writer))
			return;
		User A = registered.get(userA);
		Hashtable<String,Long> inputA = A.getInputRequest();
		Enumeration<String> keys =  inputA.keys();
		System.out.println("le richieste di " + userA + " sono:");
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			System.out.println(key);
			writer.write(key);
			writer.newLine();
		}
		writer.write("END");
		writer.newLine();
		writer.flush();
			
		String userB = reader.readLine();
		String choice = reader.readLine();
		String response;
		if(inputA.containsKey(userB)){
			if(choice.equals("SI")){
				User B = registered.get(userB);	
				addFriend(A, B);
				deleteRequests(A,B);
				response = "amicizia confermata";
			} else {
				response = "amicizia negata";
			}
		} else {
			response = "richiesta di amicizia inesistente";
		}
		writer.write(response);
		writer.newLine();
		writer.flush();
	}
	
	private void handleShowFriendList(BufferedReader reader, BufferedWriter writer) throws IOException{
		String userA = reader.readLine();
		if(!checkToken(userA, reader, writer))
			return;
		User A = registered.get(userA);
		ArrayList<User> friendA = A.getFriends();
		for(int i = 0; i < friendA.size(); i++){
			User b = friendA.get(i);
			String msg = b.getUsername() + " - " + (b.isOnline()?"online":"offline"); 
			writer.write(msg);
			writer.newLine();
		}
		writer.write("END");
		writer.newLine();
		writer.flush();
	}
	
	private void handleSearchUser(BufferedReader reader, BufferedWriter writer) throws IOException{
		String user = reader.readLine();
		if(!checkToken(user, reader, writer))
			return;
		String toFind = reader.readLine();
		Enumeration<User> users = registered.elements();
		while(users.hasMoreElements()){
			User u = users.nextElement();
			if(u.getUsername().contains(toFind)){
				writer.write(u.getUsername());
				writer.newLine();
			}
		}
		writer.write("END");
		writer.newLine();
		writer.flush();
	}
	
	private void handlePublishContent(BufferedReader reader, BufferedWriter writer) throws IOException {
		String user = reader.readLine();
		if(!checkToken(user, reader, writer))
			return;
		String toPublish = reader.readLine();
		ArrayList<User> followers = registered.get(user).getFollowers();
		if(followers != null){
			Iterator<User> i = followers.iterator();
			while(i.hasNext()){
				IClient c = i.next().getCallbackObj();
				c.notifyContent(user + ": " + toPublish);
			}
		}
	}
	
	private void handleLogout(BufferedReader reader) throws IOException{
		String userA = reader.readLine();
		User A = registered.get(userA);
		A.setOnline(false);
		int port = A.getListeningPort();
		synchronized(clientPorts){
			clientPorts[port] = false;
		}
		A.setListeningPort(-1);
		A.setUID(null, 0);
		A.setClient(null);
	}
	
	// assume che ci siano sempre porte disponibili, quindi che non si connettano più di (65536 - 1024) client
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
	
	
	private synchronized void addFriend(User a, User b){
		a.getFriends().add(b);
		b.getFriends().add(a);
	}
	
	private synchronized void deleteRequests(User a, User b){
		a.getInputRequest().remove(b.getUsername());
		b.getOutputRequest().remove(a.getUsername());
	}
	
	/* prende in input il token e verifica se è valido */
	private boolean checkToken(String name, BufferedReader reader, BufferedWriter writer) throws IOException{
		String key = reader.readLine();
		Token token = registered.get(name).getUID();
		boolean response = token.isValid() && token.getKey().equals(key);
		writer.write(new String(""+response));
		writer.newLine();
		writer.flush();
		return response;
	}
	
}
