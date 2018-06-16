import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
	
	public static void main(String args[]){
		ICongress serverObject;
		Remote remoteObject;
		
		if(args.length < 1){
			System.out.println("usage: java Client port");
			return;
		}
		
		Scanner scan = null;
		BufferedReader reader;
		try{
			Registry r = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
			remoteObject = r.lookup("GIORNATA");
			serverObject = (ICongress) remoteObject;
			
			int x = -1;
			//scan = new Scanner(System.in);
			reader = new BufferedReader(new InputStreamReader(System.in));
			while(x != 0){
				System.out.println("1 per vedere il programma della giornata");
				System.out.println("2 per iscrivere uno speaker");
				System.out.println("0 per uscire");
				//x = scan.nextInt();
				x = Integer.parseInt(reader.readLine()); 
				switch(x){
					case 1: 
						vediProgramma(serverObject, reader);
						break;
					case 2: 
						iscriviSpeaker(serverObject, reader);
						break;
					case 0:
						break;
					default:
						System.out.println("comando non valido: " + x);
						break;
				}
			}
		}catch(Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
		}finally{
			if (scan != null) scan.close();
		}
	}
	
	private static void vediProgramma(ICongress serverObject, BufferedReader r) throws RemoteException{
		System.out.println("scegli la giornata cui vuoi visualizzare il programma");
			int x = -1;
			try {
				x = Integer.parseInt(r.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(serverObject.visualizza(x));
	}
	
	private static void iscriviSpeaker(ICongress serverObject, BufferedReader r) throws RemoteException{
		String speaker = "";
		int giornata = -1;
		int sessione = -1;
		System.out.println("inserisci speaker, giornata e sessione");
		try {
			speaker = new String(r.readLine());
			giornata = Integer.parseInt(r.readLine());
			sessione = Integer.parseInt(r.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		serverObject.iscrizione(speaker, giornata, sessione);
	}
	
}
