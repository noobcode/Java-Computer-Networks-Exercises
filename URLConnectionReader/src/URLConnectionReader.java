import java.net.*;
import java.io.*;

public class URLConnectionReader {

	public static void main(String[] args) throws Exception{
		// create and open connection
		URL oracle = new URL("http://oracle.com/");
		URLConnection uc = oracle.openConnection();
		//uc.setDoOutput(true);
		//uc.setDoInput(true);
		uc.connect();
	
		// write
		OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
		out.write("string=" + "ciao");
		out.close();
		
		//read
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String line;
		while((line = in.readLine()) != null)
			System.out.println(line);
		in.close();
		
		

	}

}
