import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

public class ServerChat {

	public static void main(String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		
		// indirizzo e numero di porta del gruppo
		InetAddress ia = null;
		int port = 0;
		try{
			ia = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		}catch(IOException e){
			System.out.println("usage: java ServerChat multicast_group port");
			System.exit(1);
		}
		
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		ServerSocket serverSocket = null;
		Socket s = null;
		int portTCP = 5000;
		try{
			serverSocket = new ServerSocket(portTCP);
			while(true){
				s = serverSocket.accept();
				System.out.println("connessione TCP stabilita");
				pool.execute(new TaskServer(s, datagramSocket, ia, port));
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally{
			pool.shutdown();
			try {
				serverSocket.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
