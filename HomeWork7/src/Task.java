import java.io.*;
import java.net.*;

public class Task implements Runnable{
	
	private Socket socket;
	
	public Task(Socket s){
		socket = s;
	}
	
	public void run(){
		BufferedReader reader = null;
		BufferedWriter writer = null;
		BufferedReader fileReader = null;
		String line;
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String nomefile = reader.readLine();
			fileReader = new BufferedReader(new FileReader(nomefile));
			
			//invio il file linea per linea
			while((line = fileReader.readLine()) != null){
				writer.write(line);
				writer.newLine();
				writer.flush();
			}
			
		} catch (IOException e) {
			System.out.println("errore apertura streams!");
			e.printStackTrace();
		} finally{
			try {
				reader.close();
				writer.close();
				fileReader.close();
			} catch (IOException e) {
				System.out.println("errore chiusura streams!");
				e.printStackTrace();
			}
		}
		
	}
	
}
