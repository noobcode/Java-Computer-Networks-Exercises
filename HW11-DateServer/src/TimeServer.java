import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeServer {

	public static void main(String[] args) {
		// indirizzo e numero di porta del gruppo
		InetAddress ia = null;
		int port = 0;
		try{
			ia = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		}catch(IOException e){
			System.out.println("usage: java TimeServer multicast_group port");
			System.exit(1);
		}
		
		tipoIndirizzo(ia);
		
		byte[] data;
		int intervallo = 1000; 
		DatagramSocket ms = null;
		try {
			ms = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			try{
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				data = dateFormat.format(cal.getTime()).getBytes();
				DatagramPacket dp = new DatagramPacket(data, data.length, ia, port);
				ms.send(dp);
			}catch(IOException e){
				e.printStackTrace();
			} finally{
				ms.close();
			}
			
			try {
				Thread.sleep(intervallo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static void tipoIndirizzo(InetAddress ia){
		if(ia.isMulticastAddress()){
			if(ia.isMCGlobal()){
				System.out.println("è un indirizzo di multicast globale");
			}else if(ia.isMCOrgLocal()){
				System.out.println("è un indirizzo organization local");
			}else if(ia.isMCSiteLocal()){
				System.out.println("è un indirizzo site local");
			}else if(ia.isMCLinkLocal()){
				System.out.println("è un indirizzo link local");
			}else
				System.out.println("non è un indirizzo di multicast");
		}
	}

}
