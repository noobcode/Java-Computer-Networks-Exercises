import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotifyEventInterface extends Remote{
	
	/* metodo invocato dal server per notificare un evento ad un
	 * client remoto.
	 * è il metodo esportato dal client e che viene utilizzato dal server
	 * per la notifica di una nuova quotazione del titolo azionario.
	 */
	public void notifyEvent(int value) throws RemoteException;
	
}
