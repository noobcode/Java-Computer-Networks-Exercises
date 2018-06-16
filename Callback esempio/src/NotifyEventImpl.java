import java.rmi.RemoteException;

public class NotifyEventImpl implements NotifyEventInterface{
	
	// crea un nuovo callback client
	public NotifyEventImpl() throws RemoteException{
		super();
	}
	
	@Override
	public void notifyEvent(int value) throws RemoteException {
		String returnMessage = "update event received: " + value;
		System.out.println(returnMessage);
	}
	
}
