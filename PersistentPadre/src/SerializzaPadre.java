import java.io.*;

public class SerializzaPadre {

	public static void main(String[] args) {
		Figlio a = new Figlio("Carlo", "Alessi");
		Figlio b = new Figlio("Stefania", "Alessi");
		Figlio c = new Figlio("Silvia", "Alessi");
		Padre p = new Padre("Franco", "Alessi");
		
		//for(int i = 0; i < Integer.MAX_VALUE; i++)
			p.addFiglio(a); p.addFiglio(b); p.addFiglio(c);
		
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream("family.dat"));
		} catch (FileNotFoundException e) {
			System.out.println("impossibile trovare il file");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("errore I/O");
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			oos.writeObject(p);
		} catch (IOException e) {
			System.out.println("impossibile serializzare l'oggetto " + p);
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			oos.close();
		} catch (IOException e) {
			System.out.println("impossibile chiudere lo stream");
			e.printStackTrace();
		}
		System.out.println("serializzazione completata.");
	}

}
