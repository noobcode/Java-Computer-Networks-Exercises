import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface{

	private static final long serialVersionUID = 1L;
	private static final String message = "hello world";
	private static final int registryPort = 1099;
	
	protected RmiServer() throws RemoteException {
		super();
	}

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(registryPort);
			RmiServer obj = new RmiServer();
			Registry r = LocateRegistry.getRegistry(registryPort);
			r.bind("//localhost/RmiServer", obj);
		} catch (RemoteException e) {
			System.out.println("il registry esiste già");
		} catch (AlreadyBoundException e) {
			System.out.println("oggetto già registrato");
		}
		

	}

	@Override
	public String getMessage() throws RemoteException {
		return message;
	}

}
