//import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EUStatsClient {

	public static void main(String[] args) {
		EUStatsService serverObject;
		Remote remoteObject;
		
		if(args.length < 2){
			System.out.println("usage: java EUStatsClient port countryName");
			return;
		}
		
		/*set a security manager */
		//System.setSecurityManager(new RMISecurityManager());
		try{
			Registry r = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
			remoteObject = r.lookup("EUSTATS-SERVER");
			serverObject = (EUStatsService) remoteObject;
			/* EUStatsServiceImpl is the server implementation of the EUStatsService. the client neither knows or care about the server's
			 * implementation, only the interface. */
			
			System.out.println(args[1]);
			System.out.println("language: " + serverObject.getMainLanguages(args[1]));
			System.out.println("capital: " + serverObject.getCapitalName(args[1]));
			System.out.println("population: " + serverObject.getPopulation(args[1]));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
