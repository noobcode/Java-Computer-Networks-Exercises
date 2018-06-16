package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class KeepAlive implements Runnable{
	
	private String username;
	private static final String group = "224.0.0.19";
	private static final int portGroup = 1234;
	private static final int remotePortUDP = 5000;
	
	public KeepAlive(String usr){
		username = usr;
	}
	
	public void run(){
		MulticastSocket multicastSocket = null;
		DatagramSocket datagramSocket = null;
		byte[] bufferOut = new byte[32];
		byte[] bufferIn = new byte[1];
		
		try {
			multicastSocket = new MulticastSocket(portGroup);
			multicastSocket.joinGroup(InetAddress.getByName(group));
			datagramSocket = new DatagramSocket();
			InetAddress address = InetAddress.getByName("localhost");
			while(true){
				DatagramPacket dpIn = new DatagramPacket(bufferIn, bufferIn.length);
				multicastSocket.receive(dpIn); // riceve datagramma dal gruppo di multicast
				
				bufferOut = username.getBytes();
				DatagramPacket dpOut = new DatagramPacket(bufferOut,bufferOut.length, address, remotePortUDP);
				datagramSocket.send(dpOut); // invia datagramma al server
			}
		} catch(IOException e){
			System.out.println("error: errore di I/O. " + e.getMessage());
		} finally {
			if(multicastSocket != null) multicastSocket.close();
			if(datagramSocket != null) datagramSocket.close();
		}
	}
}