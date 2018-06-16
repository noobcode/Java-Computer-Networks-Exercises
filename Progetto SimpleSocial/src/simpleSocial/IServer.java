package simpleSocial;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {
	
	/**
	 * l'utente ad ogni login registra la callback.
	 * @param user, nome dell'utente
	 * @param c,	oggetto che rappresenta il client
	 * @throws RemoteException
	 */
	public void registerForCallback(String user, IClient c) throws RemoteException;
	
	/**
	 * l'utente A inserisce il nome di un suo amico B.
	 * il risultato è che A diventa follower di B se A e B sono amici. 
	 * @param username,	nome dell'utente 
	 * @param toFollow,	nome dell'utente da seguire
	 * @throws RemoteException
	 */
	public void follow(String username, String toFollow) 	throws RemoteException;  
	
}
