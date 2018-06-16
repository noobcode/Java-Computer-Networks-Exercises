import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote{
	public String getMessage() throws RemoteException;
}
