package simpleSocial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private ArrayList<User> friends;				// lista di amici
	private ArrayList<User> followers; 				// lista dei follower [chi sta seguendo l'User u] (follower => friend) 
	private LinkedList<String> messagesToSend; 		// messaggi generati quando l'User u era offline.
	private Hashtable<String, Long> inputRequest;	// richieste di amicizia in arrivo
	private transient Token UID;					// token di autenticazione
	private transient long lastKeepAlive;			// tempo in millisecondi dell'ultimo messaggio di keepAlive rispedito
	private transient int listeningPort;			// porta di ascolto per connessioni TCP dal server al client
	private transient IClient callbackObj;			// per registrare la callback
	
	public User(String user, String psw){
		username 		= user;
		password 		= psw;
		friends 		= new ArrayList<User>();
		followers 		= new ArrayList<User>();
		inputRequest 	= new Hashtable<String, Long>();
		messagesToSend	= new LinkedList<String>();
		lastKeepAlive 	= System.currentTimeMillis();
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

	/* l'utente è online se sono passati meno di 20 secondi dall'ultimo messaggio di keepAlive rispedito */
	public boolean isOnline() {
		long time = System.currentTimeMillis(); 
		return (time - lastKeepAlive <= 20000);
	}

	public Token getUID() {
		return UID;
	}

	public void setUID(UUID k, long l) {
		UID = new Token(k,l);
	}

	public Hashtable<String, Long> getInputRequest() {
		return inputRequest;
	}

	public void setInputRequest(Hashtable<String, Long> inputRequest) {
		this.inputRequest = inputRequest;
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

	public LinkedList<String> getMessagesToSend() {
		return messagesToSend;
	}

	protected class Token{
		private UUID key;
		private long time;
		
		public Token(UUID k, long t){
			key = k;
			time = t;
		}
		
		public UUID getKey(){
			return key;
		}
		
		public void setKey(UUID u){
			key = u;
		}
		
		public void setTime(long t){
			time = t;
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
