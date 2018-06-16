import java.io.IOException;
import java.net.*;
import java.util.Random;

public class PingServer {

	public static void main(String[] args) {
		int port = 0;
		double seed = 0;
		byte[] buf = null;;
		
		checkArgs(args);
		System.out.println("server attivato");
		
		try{
			port = Integer.parseInt(args[0]);
			if(args[1] != null)	seed = Double.parseDouble(args[1]);
		}catch(NumberFormatException e){
			if(seed == -1){
				System.out.println("ERR -arg 0");
			} else {
				System.out.println("ERR -arg 1");
			}
			e.printStackTrace();
			System.exit(1);
		}
		DatagramSocket socket = null;
		Random rand = new Random((int)seed);
		String action = null;
		try {
			socket = new DatagramSocket(port);
			buf = new byte[30];
			for(int i = 0; i < 10; i++){
				// riceve il pacchetto
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				socket.receive(dp);
				// simula probabilità di packet loss
				Boolean b = rand.nextInt(25) == 0; // b è vero con probabilità 1/25
				String data = new String(dp.getData(),"UTF-8");
		
				int delay = 0;
				if(b == true){
					action = "not sent";
				}else{
					delay = rand.nextInt(3000);
					action = "delayed " + delay + " ms";
				}
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				StringBuffer buff = new StringBuffer();
				buff.append(dp.getAddress().getHostAddress());
				buff.append(" " + dp.getPort());
				buff.append(" " + data);
				buff.append(" action: " + action);
				System.out.println(buff.toString());
				socket.send(new DatagramPacket(buf,buf.length, dp.getAddress(), dp.getPort()));
			}		
			
		} catch (SocketException e1) {
			System.out.println("server's socket error");
			e1.printStackTrace();
			System.exit(1);
		} catch(IOException e2){
			System.out.println("I/O error");
			e2.printStackTrace();
			System.exit(1);
		}finally {
			if(socket != null) socket.close();
		}
		
	}

	private static void checkArgs(String[] args){
		String usage = "java PingServer port [seed]";
		if(args.length < 1 || args.length > 2){
			System.out.println(usage);
			System.exit(1);
		}
	}
	
}
