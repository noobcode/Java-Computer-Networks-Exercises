package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TaskConnection implements Runnable {

	ServerSocket listeningSocket;
	
	public TaskConnection(ServerSocket s){
		listeningSocket = s;
	}
	
	public void run(){
		Socket s = null;
		BufferedReader reader = null;
		
		try {
			while(true){
				s = listeningSocket.accept();
				reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String msg = reader.readLine();
				System.out.println(msg);
			}
		} catch (IOException e) {
			System.out.println("error: errore di I/O. " + e.getMessage());
		} finally {
			try {
				if(reader != null) reader.close();
				if(s != null) s.close();
			} catch(IOException e){
				System.out.println("error: errore chiusura stream. " + e.getMessage());
			}
		}
	}
	
}
