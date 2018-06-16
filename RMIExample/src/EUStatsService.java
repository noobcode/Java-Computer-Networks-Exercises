import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EUStatsService extends Remote {
	String getMainLanguages(String CountryName) throws RemoteException;
	int getPopulation(String CountryName) throws RemoteException;
	String getCapitalName(String CountryName) throws RemoteException;

}

/*
 * STEP 1: DEFINIZIONE DELL'INTERFACCIA REMOTA	
 * interfaccia è remota solo se estende java.rmi.Remote e i metodi sollevano 
 * eccezione della classe RemoteException.
 * 
 * */
 