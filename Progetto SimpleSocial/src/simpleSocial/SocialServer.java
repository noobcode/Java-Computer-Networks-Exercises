package simpleSocial;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import simpleSocial.User.Token;

public class SocialServer extends UnicastRemoteObject implements IServer, Serializable{	

	private static final long serialVersionUID = 1L;
	private Hashtable <String, User> registered; 					// elenco degli utenti registrati <username -> User>							
	private static boolean[] 	clientPorts 	= setAllFalse();	// porte da assegnare ai Welcoming Socket dei client
	public  static final String SERVICE_NAME 	= "Server";			// nome dell'oggetto esportato 
	private static final int 	LISTENING_PORT  = 52609;			// porta del Welcoming Socket
	private static final String GROUP 		 	= "224.0.0.19";		// indirizzo del gruppo di multicast
	private static final int 	PORT_GROUP 		= 1234;				// porta del gruppo di multicast
	private transient KeepAliveSender kas ;							// thread che si occupa dei messaggi di keepAlive
	private transient Hashtable <UUID, User> online; 				// elenco degli utenti online 	  <token -> User>
	private transient ThreadPoolExecutor pool;
	private transient Serializer serializer;						// thread che si occupa della serializzazione
	
	public SocialServer() throws RemoteException {
		super();
		registered = new Hashtable<String, User>();
	}
	

	public static void main(String[] args) {
		SocialServer server = null;
		try {
			// prova a vedere se c'è un file che contiene la serializzazione del server
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("server.dat"));
			server = (SocialServer) ois.readObject();
			ois.close();
		} catch (Exception e) {
			// se non c'è lo inizializza per la prima volta
			System.out.println("non è presente la serializzazione del server. " + e.getMessage());
			try {
				server = new SocialServer();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} finally {
			server.online      = new Hashtable<UUID, User>();
			server.kas		   = new KeepAliveSender(server.registered, GROUP, PORT_GROUP);
			server.serializer  = new Serializer(server);
			server.pool		   = (ThreadPoolExecutor) Executors.newCachedThreadPool();
			server.kas.setDaemon(true);	
			server.serializer.setDaemon(true);
			// esporta oggetto
			server.setUpRMI();
		}
		// inizia ad ascoltare richieste
		server.startListening();
	}

	private void startListening() {
		Socket client 			  = null;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
			kas.start();
			serializer.start();
			while(true){
				client = serverSocket.accept();
				updateOnlineList();
				pool.execute(new ClientHandler(this, client, registered, clientPorts, online, GROUP, PORT_GROUP));
			}
		} catch (IOException e) {
			System.out.println("error: errore di I/O. " + e.getMessage());
		} finally {
			pool.shutdown();
			if(serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("error: errore di I/O. " + e.getMessage());
				}
		}
		
	}
	
	/**
	 * imposta come 'libere' tutte le porte dei Welcoming Socket dei client
	 * @return
	 */
	private static boolean[] setAllFalse(){
		boolean[] ports = new boolean[65535];
		for(int i = 0; i < ports.length; i++)
			ports[i] = false;
		return ports;
	}
	
	/**
	 * ad ogni nuova richiesta viene aggiornata la lista di utenti online
	 */
	private void updateOnlineList(){
		if(registered.size() == 0 || online.size() == 0)
			return;
		Enumeration<UUID> uuid = online.keys();
		while(uuid.hasMoreElements()){
			UUID userID = uuid.nextElement();
			User u = online.get(userID);
			Token userToken = online.get(userID).getUID();
			// se il token non è valido,  oppure l'utente ha chiuso l'applicazione senza fare logout
			if(!userToken.isValid() || !userID.equals(userToken.getKey()) || !u.isOnline()){
				// viene rimosso dalla lista degli utenti online
				synchronized(online){
					online.remove(userID);
					System.out.println("rimosso " + u.getUsername());
				}
				// rimuovo la callback registrata e invalido il token.
				synchronized(u){
					u.setCallbackObj(null);
					u.setUID(null);
				}
			}
		}
	}

	@Override
	public void registerForCallback(String user, IClient c) throws RemoteException {
		User u = registered.get(user);
		synchronized(u){
			u.setCallbackObj(c);
		}
		// invia i messaggi generati mentre l'User u era offline, se ci sono
		sendMessages(u);
	}
	
	private void sendMessages(User u) throws RemoteException {
		LinkedList<String> messagesToSend = u.getMessagesToSend();
		IClient c = u.getCallbackObj();
		synchronized(messagesToSend){
			while(!messagesToSend.isEmpty()){
				String msg = messagesToSend.removeFirst();	
				c.notifyContent(msg);
			}
		}	
	}

	@Override
	public void follow(String username, String toFollow) throws RemoteException {
		User userA = registered.get(username);
		User userB = registered.get(toFollow);
		// se A e B sono amici allora A può seguire B
		if(userA.getFriends().contains(userB)){
			ArrayList<User> followers = userB.getFollowers();
			synchronized(followers){
				followers.add(userA);
			}
		}
		
		// serializza
		serializer.serialize();
		synchronized(serializer){
			serializer.notify();
		}
	}
	
	/**
	 * esportazione dell'oggetto server
	 */
	private void setUpRMI(){
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			LocateRegistry.getRegistry(Registry.REGISTRY_PORT).rebind(SERVICE_NAME, this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	public Serializer getSerilizer(){
		return serializer;
	}
}
