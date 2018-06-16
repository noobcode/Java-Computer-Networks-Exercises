import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ThreadReaderMulticast extends Thread {

	private MulticastSocket msocket;
	
	public ThreadReaderMulticast(MulticastSocket s){
		msocket = s;
	}
	
	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		while(true){
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
			// si blocca sulla ricezione di un messaggio
			try {
				msocket.receive(dp);
				System.out.println(">> " + new String(dp.getData()));
			} catch (IOException e){
				e.printStackTrace();
			}
			int len = dp.getLength();
			for(int i = 0; i < len; i++)
				buffer[i] = 0;
		}
		
	}

}
