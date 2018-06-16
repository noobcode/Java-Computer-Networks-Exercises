import java.net.*;
import java.io.*;

public class TimeClient {

	public static void main(String[] args) {
		InetAddress group = null;
		int port = 0;
		
		try{
			group = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		}catch(Exception e){
			System.out.println("usage: java TimeClient multicast_address port");
			System.exit(1);
		}
		MulticastSocket ms = null;
		try{
			ms = new MulticastSocket(port);
			ms.joinGroup(group);
			byte[] buffer = new byte[1024];
			
			for(int i = 0; i < 10; i++){
				try{
					DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
					ms.receive(dp);
					String s = new String(dp.getData());
					System.out.println(s);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(ms != null){
				try {
					ms.leaveGroup(group);
					ms.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
