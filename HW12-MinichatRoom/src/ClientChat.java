import java.io.*;
import java.net.*;
import java.util.*;

public class ClientChat {

	public static void main(String[] args) {
		InetAddress group = null;
		int portGroup = 0;
		int clientID = 0; 
		
		try{
			group = InetAddress.getByName(args[0]);
			portGroup = Integer.parseInt(args[1]);
			clientID = Integer.parseInt(args[2]);
		}catch(Exception e){
			System.out.println("usage: java ClientChat multicast_address port ClientID");
			System.exit(1);
		}
		
		// aderisce al gruppo multicast per ricevere messaggi dal server
		MulticastSocket msocket = null;
		try {
			msocket = new MulticastSocket(portGroup);
			msocket.joinGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// si connette al server con una connessione TCP
		String host = "localhost";
		int portTCP = 5000;
		Socket socketTCP = null;
		try {
			socketTCP = new Socket(host, portTCP);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// coda di messaggi condivisa
		ArrayList<String> msgQueue = new ArrayList<String>();
		ThreadReader reader = new ThreadReader(msgQueue, clientID);
		ThreadSender sender = new ThreadSender(msgQueue, socketTCP, clientID);
		ThreadReaderMulticast readerMulticast = new ThreadReaderMulticast(msocket);
		readerMulticast.setDaemon(true);
		reader.start();
		sender.start();
		readerMulticast.start();
		
		try{
			reader.join();
			sender.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
