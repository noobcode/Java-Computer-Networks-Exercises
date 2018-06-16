import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class URL2 {

	public static void main(String[] args) throws Exception{
		InetAddress remoteIP = InetAddress.getByName("www.lastampa.it");
		Socket s = new Socket(remoteIP, 80);
	    BufferedReader dIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    PrintStream dOut = new PrintStream(s.getOutputStream());
	    dOut.println("GET /italia/cronache HTTP/1.1");
	    dOut.println();
	    String str = null;
	    do {
	        str = dIn.readLine();
	        System.out.println(str);
	    } while (str != null);

	}

}
