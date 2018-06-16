import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception{
		int nPersone;
		int nSportelli = 4; // pool Size
		int k; // capacità seconda stanza di attesa
		
		Scanner scan;
		Ufficio ufficio;
		
		System.out.println("numero persone?");
		scan = new Scanner(System.in);
		nPersone = scan.nextInt();
		System.out.println("capienza seconda stanza?");
		k = scan.nextInt();
		
		// crea l'ufficio
		ufficio = new Ufficio(nPersone, nSportelli, k);
		ufficio.start(); // l'ufficio apre
		
		// mette le persone nella sala d'attesa 1
		for(int i = 0; i < nPersone; i++){
			ufficio.putClientInWaitingRoom( new Persona("number_" + i));
		}
		
		// l'ufficio rimane aperto 5 secondi
		//Thread.sleep(5000);
		ufficio.closeOffice();
		scan.close();
	}
}