import java.io.*;

public class FlattenTime {

	public static void main(String[] args) {
		String filename = "time.ser";
		PersistentTime time = new PersistentTime();
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(time);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
