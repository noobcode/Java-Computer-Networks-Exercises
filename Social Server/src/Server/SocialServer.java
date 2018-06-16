package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import Client.IClient;
import Server.User.Token;

public class SocialServer extends UnicastRemoteObject implements IServer{	

	private static final long serialVersionUID = 1L;
	private static Hashtable <String, User> registered = new Hashtable<String, User>();;
	public static final String SERVICE_NAME = "Server";
	private static final int listeningPort = 52609;

	protected SocialServer() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket client = null;
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();	
		boolean[] clientPorts = new boolean[65535];
		setAllFalse(clientPorts);
		Random rand = new Random(1000); // MAI USATO 
		
		Thread t1 = new Thread(new KeepAliveSender(registered));
		t1.setDaemon(true);
		
		SocialServer server = setUpRegistry(); // eventualmente metti void		
		
		try {
			serverSocket = new ServerSocket(listeningPort);
			t1.start();
			while(true){
				client = serverSocket.accept();
				updateOnlineList(registered);
				pool.execute(new Task(client, registered, clientPorts, rand));
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
	
	private static SocialServer setUpRegistry() {
		SocialServer server = null;
		try {
			server = new SocialServer();
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			LocateRegistry.getRegistry(Registry.REGISTRY_PORT).rebind(SERVICE_NAME, server);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return server;
	}

	private static void setAllFalse(boolean[] ports){
		for(int i = 0; i < ports.length; i++)
			ports[i] = false;
	}
	
	private static void updateOnlineList(Hashtable<String,User> registered){
		if(registered.size() == 0) 
			return;
		boolean valid;
		Enumeration<User> users = registered.elements();
		while(users.hasMoreElements()){
			User u = users.nextElement();
			Token tok = u.getUID();
			if(tok == null)
				valid = false;
			else {
				valid = tok.isValid();
			}
			if(!valid)
				synchronized(u){
					u.setOnline(false);
				}	
		}
	}

	@Override
	public void registerForCallback(String user, IClient c) throws RemoteException {
		registered.get(user).setCallbackObj(c);
	}

	@Override
	public void follow(String username, String toFollow) throws RemoteException {
		ArrayList<User> followers = registered.get(toFollow).getFollowers();
		synchronized(followers){
			followers.add(registered.get(username));
		}
	}
	
}
