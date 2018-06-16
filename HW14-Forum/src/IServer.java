import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote{
	
	public void aperturaForum(INotifyMessage client, String forum) throws RemoteException;
	
	public void nuovoMessaggio(String forum, String messaggio) throws RemoteException;
		
}