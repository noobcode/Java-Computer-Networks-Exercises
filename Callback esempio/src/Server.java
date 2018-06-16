import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(5000);
			Registry r = LocateRegistry.getRegistry(5000);
			//registrazione presso il registry
			ServerImpl server = new ServerImpl();
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 39000);
			r.bind("Server", stub);
			
			while(true){
				int val = (int) (Math.random()*1000);
				System.out.println("nuovo updae " + val);
				server.update(val);
				Thread.sleep(1500);
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("eccezione " + e);
		}
	}

}
