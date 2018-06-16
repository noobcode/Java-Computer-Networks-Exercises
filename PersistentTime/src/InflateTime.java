import java.io.*;
import java.util.Calendar;

public class InflateTime {

	public static void main(String[] args) {
		String filename = "time.ser";
		PersistentTime time = null;
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(filename));
			time = (PersistentTime) ois.readObject();
			ois.close();
		} catch (IOException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e){ e.printStackTrace(); }
		
		System.out.println("flattened time: " + time.getTime());
		System.out.println("current time: " + Calendar.getInstance().getTime());
	}

}
