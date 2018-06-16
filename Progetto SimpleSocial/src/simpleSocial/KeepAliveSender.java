package simpleSocial;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Hashtable;

public class KeepAliveSender extends Thread{
	
	private Hashtable<String, User> registered;		// riferimento alla tabella degli utenti registrati
	private final String GROUP;						// gruppo di multicast
	private final int 	 GROUP_PORT;				// porta del gruppo di multicast
	private final int 	 UDP_PORT = 5000;			// porta locale per ricevere messaggi UDP
	
	public KeepAliveSender(Hashtable<String,User> reg, String g, int p){
		registered = reg;
		GROUP = g;
		GROUP_PORT = p;
	}
	
	public void run(){
		int interval 		= 10000;			// intervallo di 10 secondi
		DatagramSocket ds 	= null;
		byte[] bufferIn 	= new byte[32];		// buffer per ricevere
		byte[] bufferOut 	= new byte[1];		// buffere per inviare
		long count 			= 0;				// contatore degli utenti online
		
		try {
			ds = new DatagramSocket(UDP_PORT);
			ds.setSoTimeout(interval);
			while(true){
				if(registered.size() > 0){
					sendKA(ds, bufferOut);
					count = receiveKA(ds, bufferIn, System.currentTimeMillis(), interval);
					System.out.println("il numero di utenti online è " + count);
				}
				sleep(interval);
			}
		} catch(IOException | InterruptedException e){
			e.printStackTrace();
		} finally {
			if(ds != null) ds.close();
		}
		
	}	
	
	/**
	 * invia il messaggio di keepAlive al gruppo di multicast
	 * @param ds
	 * @param bufferOut
	 * @throws IOException
	 */
	private void sendKA(DatagramSocket ds, byte[] bufferOut) throws IOException{
		bufferOut = "?".getBytes();
		DatagramPacket dp = new DatagramPacket(bufferOut, bufferOut.length, InetAddress.getByName(GROUP), GROUP_PORT);
		ds.send(dp);
	}
	
	/**
	 * aspetta per 10 secondi le risposte ai messaggi di keepAlive
	 * @param ds
	 * @param bufferIn
	 * @param keepAliveSession
	 * @param interval
	 * @return msgCount, il numero di utenti che hanno risposto al messaggio di keepAlive
	 * @throws IOException
	 */
	private long receiveKA(DatagramSocket ds, byte[] bufferIn, long keepAliveSession, long interval) throws IOException{
		long msgCount 	 = 0;
		long currentTime = System.currentTimeMillis();
		
		// finché non sono passati 10 secondi dall'inizio della sessione di keepAlive
		while(currentTime - keepAliveSession < interval){
			DatagramPacket dp = new DatagramPacket(bufferIn, bufferIn.length);
			try {
				ds.receive(dp);
			} catch(SocketTimeoutException e){
				System.out.println("timeout scaduto");
				break;
			}
			msgCount++;
			String username =  new String(dp.getData(), 0, dp.getLength());
			User u = registered.get(username);
			synchronized(u){
				u.setLastKeepAlive(System.currentTimeMillis());
			}
		}
		return msgCount;
	}

}
