import java.io.*;
import java.net.*;

public class TaskServer implements Runnable {

	private Socket socket;
	private DatagramSocket datagramSocket;
	private InetAddress ia;
	private int port;
	
	public TaskServer(Socket socket, DatagramSocket datagramSocket, InetAddress add, int p){
		this.socket = socket;
		this.datagramSocket = datagramSocket;
		ia = add;
		port = p;
	}
	
	@Override
	public void run() {
		BufferedReader reader = null;
		System.out.println("socket tcp connesso: " + socket.isConnected());
		System.out.println("socket local port: " + socket.getLocalPort());
		System.out.println("socket remote port: " + socket.getPort());
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String line = "";
		int bufsize = 512;
		byte[] buf = new byte[bufsize];
		try {
			System.out.println("attesa lettura");
			// esce dal ciclo quando è finito lo stream (il client non invia il messaggio EXIT, chiude la connessione e basta)
			while(true){
				line = reader.readLine();
				if(line == null) break;
				if(line.length() > bufsize){
					// lo scarto
					System.out.println("line lenght = " + line.length() + "buff lenght = " + buf.length + " scartato!!!!");
				}else{
					// invia il messaggio sul gruppo multicast
					buf = line.getBytes();
					DatagramPacket dp = new DatagramPacket(buf,buf.length , ia, port);
					datagramSocket.send(dp);
					System.out.println("inviato sul gruppo di multicast: " + new String(dp.getData()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
