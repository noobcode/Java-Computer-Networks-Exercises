import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	
	// registrazione per la callback
	public void registerForCallback(NotifyEventInterface clientInterface) throws RemoteException;
	
	// cancella registrazione per la callback
	public void unregisterForCallback(NotifyEventInterface clientInterface) throws RemoteException;
}
