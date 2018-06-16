import java.rmi.*;

public interface INotifyMessage extends Remote{

	/* 
	 * invocato dal server per notificare l'arrivo di un nuovo
	 * messaggio nel forum
	 */
	public void notifyMsg(String forum, String msg) throws RemoteException;
}
