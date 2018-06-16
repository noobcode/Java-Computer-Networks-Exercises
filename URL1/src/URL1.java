import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class URL1 {

	public static void main(String[] args) throws IOException {
		BufferedReader reader;
		BufferedWriter writer;
		InetAddress remoteIP;
		String message;
		String response = "", line;
		
		remoteIP = InetAddress.getByName("www.lastampa.it"); // resolve IP address
		Socket s = new Socket(remoteIP, 80);				// TCP connection
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		// create message
		message = "GET /italia/cronache HTTP/1.1\r\n";
		message = message + "\r\n";
		// send message
		writer.write(message);
		writer.newLine();
		writer.flush();
		
		//System.out.println("start");
		
		while((line = reader.readLine()) != null)		
			System.out.println(response);
		
		System.out.println("done");
		
	}

}
