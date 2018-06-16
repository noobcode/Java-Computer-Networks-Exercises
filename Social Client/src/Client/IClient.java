package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote{
	public void notifyContent(String msg) throws RemoteException;
}
