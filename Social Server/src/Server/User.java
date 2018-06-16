package Server;

import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;

import Client.IClient;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private ArrayList<User> friends;	// lista di amici
	private ArrayList<User> followers; 	// lista dei follower [chi sta seguendo l'User u] (follower => friend) 
	private Hashtable<String, Long> inputRequest;	// richieste di amicizia in arrivo
	private Hashtable<String, Long> outputRequest;	// richieste di amicizia in uscita  SERVONO??????
	private boolean isOnline;
	private long lastKeepAlive;		// tempo in millisecondi dell'ultimo messaggio di keepAlive rispedito
	private Token UID;				// token di autenticazione
	private transient Socket client;
	private int listeningPort;		// porta di ascolto per connessioni TCP dal server al client
	private IClient callbackObj;
		
	public User(String user, String psw){
		username = user;
		password = psw;
		friends = new ArrayList<User>();
		inputRequest = new Hashtable<String, Long>();
		outputRequest = new Hashtable<String, Long>();
		isOnline = false;
		client = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<User> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<User> friends) {
		this.friends = friends;
	}

	/* l'utente è online solo se sono passati meno di 10 secondi dall'ultimo messaggio di keepAlive rispedito */
	public boolean isOnline() {
		return (System.currentTimeMillis() - lastKeepAlive <= 10000);
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public Token getUID() {
		return UID;
	}

	public void setUID(String k, long l) {
		UID = new Token(k,l);
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}	
	
	public Hashtable<String, Long> getInputRequest() {
		return inputRequest;
	}

	public void setInputRequest(Hashtable<String, Long> inputRequest) {
		this.inputRequest = inputRequest;
	}

	public Hashtable<String, Long> getOutputRequest() {
		return outputRequest;
	}

	public void setOutputRequest(Hashtable<String, Long> outputRequest) {
		this.outputRequest = outputRequest;
	}

	public void setUID(Token uID) {
		UID = uID;
	}

	public int getListeningPort() {
		return listeningPort;
	}

	public void setListeningPort(int listeningPort) {
		this.listeningPort = listeningPort;
	}
	
	public long getLastKeepAlive(){
		return lastKeepAlive;
	}
	
	public void setLastKeepAlive(long t){
		lastKeepAlive = t;
	}

	public ArrayList<User> getFollowers() {
		return followers;
	}

	public void setFollowers(ArrayList<User> followers) {
		this.followers = followers;
	}

	public IClient getCallbackObj() {
		return callbackObj;
	}

	public void setCallbackObj(IClient callbackObj) {
		this.callbackObj = callbackObj;
	}

	protected class Token implements Serializable{
		private static final long serialVersionUID = 1L;
		private String key;
		private long time;
		
		public Token(String k, long t){
			key = k;
			time = t;
		}
		
		public String getKey(){
			return key;
		}
		
		public boolean isValid(){
			long h24 = 24*60*60*1000;
			if(key == null) return false;
			if(System.currentTimeMillis() - time < h24){
				return true;
			} else
				return false;
		}		
	}
	
}
