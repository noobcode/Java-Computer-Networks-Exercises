import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

public class Server extends RemoteServer implements ICongress{

	private static final long serialVersionUID = 1L;
	private static GiornataCongresso[] giornate = null;

	public Server(){
		giornate = new GiornataCongresso[3];
		for(int i = 0; i < 3 ; i++)
			giornate[i]= new GiornataCongresso();
	}
	
	public static void main(String[] args) {
		System.out.println("server attivato");
		// creo un oggetto istanza del servizio
		Server service = new Server();
		// esportazione del servizio
		try {
			ICongress stub = (ICongress) UnicastRemoteObject.exportObject(service, 0);
			
			// creazione di un registri sulla porta indicata
			int port = 1099;
			LocateRegistry.createRegistry(port);
			Registry r = LocateRegistry.getRegistry(); // reference to a remote object registry;
			// pubblicazione dello stub nel registry
			r.rebind("GIORNATA", stub);
		} catch (RemoteException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public String iscrizione(String speaker, int giornata, int sessione) throws RemoteException {
		if(giornata < 0 || giornata >= giornate.length){
			return "no such giornata or directory";
		} else if(sessione < 0 || sessione >= giornate[0].getNumeroSessioni()){
			return "no such sessione or directory";
		}
		
		GiornataCongresso g = giornate[giornata];
		int i;
		for(i = 0; i < g.getNumeroInterventi(); i++){
			synchronized(g){
				if(g.getGiornata()[sessione][i].equals("NA")){
					g.setGiornata(sessione, i, speaker); // viene assegnato l'intervento 'i' della sessione allo speaker 
					break;
				}
			}
		}
		if(i < g.getNumeroInterventi()) return "speaker " + speaker + "registrato per " + "sessione: " + sessione + " intervento " + i;
		else return "sessione piena";
	}

	@Override
	public String visualizza(int giornata) throws RemoteException {
		if(giornata < 0 || giornata >= giornate.length){
			return "no such giornata or directory";
		} 
		GiornataCongresso g = giornate[giornata];
		return g.toString();
	}
	
}
