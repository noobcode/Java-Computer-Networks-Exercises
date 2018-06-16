import java.rmi.RemoteException;

public class NotifyMessageImpl implements INotifyMessage{

	public NotifyMessageImpl() throws RemoteException{
		super();
	}
	
	@Override
	public void notifyMsg(String forum, String msg) throws RemoteException {
		System.out.println(forum + ": " + msg);
	}

}
