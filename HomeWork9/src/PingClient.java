import java.io.IOException;
import java.net.*;

public class PingClient {

	public static void main(String[] args) {
		int timeout = 2000;
		int port = 0;
		// controllo numero argomenti
		checkArgs(args);
		
		String hostname = args[0];
		InetAddress address = null;
		try {
			address = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			System.out.println("ERR -arg 0");
			e.printStackTrace();
			System.exit(1);
		}
		
		try{
			port = Integer.parseInt(args[1]);
		}catch(NumberFormatException e){
			System.out.println("ERR -arg 1");
			e.printStackTrace();
			System.exit(1);
		}
		DatagramSocket dgSocket = null;
		try {
			dgSocket = new DatagramSocket();
			dgSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		sendMessages(dgSocket, 10, port, address);
				
	}
	
	private static void checkArgs(String[] args){
		String usage = "java PingClient hostname port";
		if(args.length != 2){
			System.out.println(usage);
			System.exit(1);
		}
	}
	
	private static void sendMessages(DatagramSocket dgSocket, int n, int port, InetAddress address){
		byte[] dataout= new byte[20];
		byte[] datain;
		double minRTT = Double.MAX_VALUE;
		double maxRTT = Double.MIN_VALUE;
		double sumRTT = 0;
		double currentRTT = 0;
		int loss = 0;
		
		for(int i = 0; i < n; i++){
			long timeSent = System.currentTimeMillis();
			String msg = "PING " + i + " " + timeSent; 
			dataout = msg.getBytes();
			DatagramPacket dp = new DatagramPacket(dataout, dataout.length, address, port);
			// invio del pacchetto
			try {
				dgSocket.send(dp);
			} catch (IOException e) {
				System.out.println("I/O error");
				e.printStackTrace();
				System.exit(1);
			}
			datain = new byte[dataout.length];
			DatagramPacket packetReceived = new DatagramPacket(datain, datain.length);		
			try {
				dgSocket.receive(packetReceived);
				currentRTT = System.currentTimeMillis() - timeSent;
				System.out.println(msg + " RTT: " + currentRTT +  "ms");
			} catch (IOException e) {
				System.out.println(msg + " RTT: *");
				try {
					currentRTT = dgSocket.getSoTimeout();
				} catch (SocketException e1) {
					e1.printStackTrace();
				}
				loss++;
			}
			
			sumRTT += currentRTT;
			if(currentRTT > maxRTT) 
				maxRTT = currentRTT;
			else if(currentRTT <= minRTT) 
				minRTT = currentRTT;
		}
		
		printStatistics(maxRTT, minRTT, sumRTT, n, loss);
	}
		
	private static void printStatistics(double maxRTT, double minRTT, double sumRTT, int n, int loss){
		System.out.println("--.PING statistics---");
		System.out.println(n + " packets transmitted, " + (n-loss) + " received, " + ((float)loss*100/n) + "% packet loss");
		System.out.printf("round-trip (ms) min/avg/max = " + (int)minRTT + "/ %.2f/ " + (int)maxRTT, (sumRTT/n));
	}
	

}
