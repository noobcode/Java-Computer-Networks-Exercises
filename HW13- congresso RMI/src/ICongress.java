import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICongress extends Remote {
	/* potrebbe restituire il numero dell'intervento per quella data giornata e sessione*/
	public String iscrizione(String speaker, int giornata, int sessione) throws RemoteException;
	/* visualizza il programma per la giornata*/
	public String visualizza(int giornata) throws RemoteException;
}
