import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

/* STEP 2: IMPLEMENTAZIONE DEL SERVIZIO
 * soluzione adottata:
 * - definizione di una classe che implementa l'interfaccia remota e che estenda la classe
 * RemoteServer. 
 * vantaggi: si eredita la semantica degli oggetti remoti da RemoteServer
 * svantaggi: non si possono estendere altre classi. */

/* 	STEP 3: ATTIVAZIONE E GENERAZIONE STUB
 * stub è un oggetto che permette di interfacciarsi con un oggetto remoto detto target.
 * lo stub inoltra le chiamate che riceve al suo target.
 * */

public class EUStatsServiceImpl extends RemoteServer implements EUStatsService{
	private static final long serialVersionUID = 1L;
	
	// store data in a hashtable
	private static Hashtable<String,EUData> EUDbase = new Hashtable<String, EUData>();
	
	// constructor, set up database
	public EUStatsServiceImpl() throws RemoteException{
		EUDbase.put("france", new EUData("French", 1000,"Paris"));
		EUDbase.put("uk", new EUData("English", 234234234, "London"));
		EUDbase.put("italy", new EUData("Italian", 234234245, "Rome"));
	}
	
	public static void main(String args[]){
		try{
			// crea un'istanza del servizio
			EUStatsServiceImpl service =  new EUStatsServiceImpl();
			/* esportazione dell'oggetto, restituisce un'istanza dell'oggetto stub,
			che rappresenta l'oggetto remoto mediante il suo riferimento. */
			EUStatsService stub = (EUStatsService) UnicastRemoteObject.exportObject(service, 0);
			
			// creazione di un registry sulla porta 
			int port = 3000;
			LocateRegistry.createRegistry(port);
			Registry r = LocateRegistry.getRegistry(port);
			
			// publicazione dello stub nel registry
			r.rebind("EUSTATS-SERVER", stub);
			EUDbase.get("italy").setCapital("foligno");
		}catch(RemoteException e){
			System.out.println("communication error " + e.toString());
		}
		/* il main termina, ma il thread in attesa di invocazione di metodi remoti rimane attivo. */
	}
	
	
	
	// implementazione dei metodi dell'interfaccia
	@Override
	public String getMainLanguages(String CountryName) throws RemoteException {
		return EUDbase.get(CountryName).getLanguage();
	}

	@Override
	public int getPopulation(String CountryName) throws RemoteException {
		return EUDbase.get(CountryName).getPopulation();
	}

	@Override
	public String getCapitalName(String CountryName) throws RemoteException {
		return EUDbase.get(CountryName).getCapital();
	}

}
