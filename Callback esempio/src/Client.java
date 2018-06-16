import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {

	public static void main(String[] args) {
		try {
			System.out.println("cerco il server...");
			Registry r = LocateRegistry.getRegistry(5000);
			ServerInterface server = (ServerInterface) r.lookup("Server");
			
			// si registra per la callback
			System.out.println("mi registro per la callback");
			NotifyEventInterface callbackObj = new NotifyEventImpl();
			NotifyEventInterface stub = (NotifyEventInterface) UnicastRemoteObject.exportObject(callbackObj, 0);
			server.registerForCallback(stub);
			
			// attende gli eventi generati dal server per un intervallo di tempo
			Thread.sleep(10000);
			
			// cancella la registrazione per la callback
			System.out.println("unregistering for callback");
			server.unregisterForCallback(stub);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("client exception: " + e.getMessage());
		}
	}
}

