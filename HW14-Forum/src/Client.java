import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {

	private static final int registryPort = 65534;
	
	public static void main(String[] args) {
		System.out.println("premi 1 per registrarti a un forum");
		System.out.println("premi 2 per scrivere un messaggio in un forum");
		System.out.println("premi 0 per uscire");
		try {
			Registry r = LocateRegistry.getRegistry(registryPort);
			IServer  server = (IServer) r.lookup("//localhost/Forum");
			INotifyMessage callbackObj = new NotifyMessageImpl();
			INotifyMessage stub = (INotifyMessage) UnicastRemoteObject.exportObject(callbackObj, 0);
			
			int op = 0;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			do {				
				op = Integer.parseInt(in.readLine());
				String forum = null;
				String msg = null;
				switch(op){
					case 1:
						System.out.println("nome forum:");
						forum = in.readLine();
						server.aperturaForum(callbackObj, forum);
						break;
					case 2:
						System.out.println("nome forum:");
						forum = in.readLine();
						System.out.println("messaggio:");
						msg = in.readLine();
						server.nuovoMessaggio(forum, msg);
						break;
					case 0:
						System.out.println("esco...");
						break;
				}
				
			} while(op != 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

}
