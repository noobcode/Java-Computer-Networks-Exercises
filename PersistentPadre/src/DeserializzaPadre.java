import java.io.*;

public class DeserializzaPadre {

	public static void main(String[] args) {
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream("family.dat"));
		} catch (FileNotFoundException e) {
			System.out.println("impossibile  trovare il file");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("errore nella creazione dello stream");
			e.printStackTrace();
			System.exit(1);
		}

		Padre p = null;
		try {
			p = (Padre)ois.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("impossibile trovare la classe");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("errore nella creazione dello stream");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(p.toString());
	}

}
