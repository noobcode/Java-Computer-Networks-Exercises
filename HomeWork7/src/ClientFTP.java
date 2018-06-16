import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientFTP {

	public static void main(String[] args) {
		Socket socket = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;
		String nomeFile = "";
		String host = "localhost";
		int port = 10000;
		
		if(args.length == 1){
			nomeFile = args[0];
		}else {
			System.out.println("usage: specificare nome file");
			System.exit(1);
		}
		
		try {
			socket = new Socket(host, port);
			// invia nomefile al server
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(nomeFile);
			writer.newLine();
			writer.flush();
			
			// riceve il file dal server
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

}
