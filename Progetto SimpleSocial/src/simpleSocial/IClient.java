package simpleSocial;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {
	
	/**
	 * il Server notifica al Client che un follower ha pubblicato un contenuto.
	 * il contenuto viene salvato in una lista interna al SocialClient, in attesa che l'utente
	 * voglia visualizzarlo.
	 * 
	 * @param msg, messaggio pubblicato dal follower 
	 * @throws RemoteException
	 */
	public void notifyContent(String msg) throws RemoteException;
}
