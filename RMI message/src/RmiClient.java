import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {

	private static final int registryPort = 1099;
	
	public static void main(String[] args) {
		try {
			Registry r = LocateRegistry.getRegistry(registryPort);
			RmiServerInterface obj = (RmiServerInterface) r.lookup("//localhost/RmiServer");
			System.out.println(obj.getMessage());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

}
