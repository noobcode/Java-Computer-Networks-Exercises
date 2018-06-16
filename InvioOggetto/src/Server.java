import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		try{
			ServerSocket server = new ServerSocket(1024);
			Socket clientSocket = server.accept();
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject("welcome");
			Studente s = new Studente(526092,"Carlo","Alessi","informatica");
			oos.writeObject(s);
			oos.writeObject("goodbye");
			clientSocket.close();
			server.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
