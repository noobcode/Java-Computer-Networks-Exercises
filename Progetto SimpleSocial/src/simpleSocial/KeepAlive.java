package simpleSocial;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class KeepAlive extends Thread{
	
	private String username;
	private static final String GROUP 			= "224.0.0.19"; // gruppo di multicast
	private static final int 	PORT_GROUP 		= 1234;			// porta del gruppo
	private static final int 	REMOTE_UDP_PORT = 5000;			// porta remota su cui rispedire i messaggi di keepAlive ricevuti
	
	public KeepAlive(String usr){
		username = usr;
	}
	
	public void run(){
		MulticastSocket multicastSocket = null;
		DatagramSocket 	datagramSocket 	= null;
		byte[] 			bufferOut 		= new byte[32];
		byte[] 			bufferIn 		= new byte[32];
		InetAddress 	address 		= null;
		InetAddress 	mGroup 			= null;
		
		try {
			mGroup = InetAddress.getByName(GROUP);
			multicastSocket = new MulticastSocket(PORT_GROUP);
			multicastSocket.joinGroup(mGroup);
			datagramSocket = new DatagramSocket();
			address = InetAddress.getByName("localhost");
		} catch(IOException e){
			e.printStackTrace();
		}
		
		while(true){
			try {
				DatagramPacket dpIn = new DatagramPacket(bufferIn, bufferIn.length);
				multicastSocket.receive(dpIn); // riceve datagramma dal gruppo di multicast 
				
				String msg = new String(dpIn.getData(), 0, dpIn.getLength());
				if(msg.equals(username))
					break;
				
				bufferOut = username.getBytes();
				DatagramPacket dpOut = new DatagramPacket(bufferOut, bufferOut.length, address, REMOTE_UDP_PORT);
				datagramSocket.send(dpOut); // invia datagramma al server
			} catch(IOException e){
				System.out.println("error: errore di I/O. " + e.getMessage());
			}
		}
			
		if(multicastSocket != null){
			try {
				multicastSocket.leaveGroup(mGroup);
			} catch (IOException e) {
				e.printStackTrace();
			}
			multicastSocket.close();
		}
			
		if(datagramSocket != null) 
			datagramSocket.close();		
	}	
}