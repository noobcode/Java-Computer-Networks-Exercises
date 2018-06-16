import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] args) {
		try{
			Socket socket = new Socket("localhost", 1024);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String beginMsg = (String)ois.readObject();
			Studente s = (Studente)ois.readObject();
			String endMsg = (String)ois.readObject();
			
			System.out.println(beginMsg);
			System.out.println(s.toString());
			System.out.println(endMsg.toString());
			socket.close();
		}catch(Exception e){
			System.out.println(e);
		}

	}

}
