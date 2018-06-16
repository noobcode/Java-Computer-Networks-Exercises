package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Hashtable;

public class KeepAliveSender extends Thread{
	
	private Hashtable<String, User> registered;
	private final String group = "224.0.0.19";
	private final int portGroup = 1234;
	private final int portUDP = 5000;
	
	public KeepAliveSender(Hashtable<String,User> reg){
		registered = reg;
	}
	
	public void run(){
		long interval = 10000;
		DatagramSocket ds = null;
		byte[] bufferIn = new byte[32];
		byte[] bufferOut = new byte[1];
		long count = 0;
		
		try {
			ds = new DatagramSocket(portUDP);
			ds.setSoTimeout((int)interval);
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
	
	
	private void sendKA(DatagramSocket ds, byte[] bufferOut) throws IOException{
		bufferOut = "?".getBytes();
		DatagramPacket dp = new DatagramPacket(bufferOut, bufferOut.length, InetAddress.getByName(group), portGroup);
		ds.send(dp);
	}
	
	private long receiveKA(DatagramSocket ds, byte[] bufferIn, long keepAliveSession, long interval) throws IOException{
		long msgCount = 0;
		long currentTime = System.currentTimeMillis();
		
		while(currentTime - keepAliveSession < interval){
			DatagramPacket dp = new DatagramPacket(bufferIn, bufferIn.length);
			System.out.println("reading...");
			try {
				ds.receive(dp);
			} catch(SocketTimeoutException e){
				System.out.println("timeout scaduto");
				break;
			}
			msgCount++;
			String username =  new String(dp.getData(), 0, dp.getLength());
			User u = registered.get(username);
			u.setLastKeepAlive(System.currentTimeMillis());
		}
		return msgCount;
	}

}
