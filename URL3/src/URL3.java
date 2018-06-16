import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URL3 {

	public static void main(String[] args) throws Exception{
		BufferedReader reader;
		String line;
		URL url = new URL("http://www.lastampa.it/italia/cronache");
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.connect();
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
		do {
			line = reader.readLine();
			System.out.println(line);
		}while(line != null);	

	}

}
