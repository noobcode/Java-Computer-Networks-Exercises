import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;

/* effettua la lettura dal gruppo di multicast e invia i messaggi al server */
public class ThreadSender extends Thread {
	private ArrayList<String> msgQueue;
	private Socket socketTCP;
	private int clientID;
	
	public ThreadSender(List<String> l, Socket sock, int id){
		msgQueue = (ArrayList<String>)l;
		socketTCP = sock;
		clientID = id;
	}
	
	public void run(){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(socketTCP.getOutputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String msg = null;
		while(true){			
			// se c'è un messaggio in coda lo estraggo...
			synchronized(msgQueue){
				while(msgQueue.isEmpty()){
					try { msgQueue.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
				}
				msg = msgQueue.remove(0);
			}
			// ...e lo invio, oppure esco
			if(msg.equals("EXIT")){ break; }
			try {
				msg = "client " + clientID + ": " + msg+"#";
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		closeAllConnections(writer, socketTCP);
	}
	
	private static void closeAllConnections(BufferedWriter w, Socket s2){
		try {
			w.close();
			s2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}