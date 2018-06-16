import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientFlickr {

	public static void main(String[] args) {
		String tagName;
		String path; 
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		int k = 0;
		
		// get user input
		Scanner s = new Scanner(System.in);
		
		System.out.println("inserire tag...");
		tagName = s.nextLine();
		System.out.println("inserire numero immagini...");
		try{ 
			k = s.nextInt();
		}catch(InputMismatchException e){
			System.out.println("inserire un intero");
			e.printStackTrace();
			System.exit(1);	
		}
		s.close();
		
		// create and open connection
		path = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tag=<"+ tagName + ">";
		URL flickr = null;
		URLConnection uc = null;
		try {
			flickr = new URL(path);
			uc = flickr.openConnection();
			uc.connect();
		} catch (MalformedURLException e) {
			System.out.println("URL mal formattato");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e){
			System.out.println("I/O error");
			e.printStackTrace();
			System.exit(1);
		}
		
		JSONObject objJson;
		JSONParser parserJson = new JSONParser();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line;
			
			// legge righe finché non arrivo all'array di oggetti che mi interessano
			while((line = in.readLine()) != null){
				if(line.equals("		\"items\": ["))
					break;
			}
			
			for(int i = 0; i < k; i++){
				line = "";
				// un oggetto foto è formato da 11 righe
				for(int j = 0; j < 11; j++){
					line = line + in.readLine();
				}
				// tolgo la virgola (,) dalla stringa letta
				line = line.substring(0, line.length()-1);
				// creo un oggetto json e lo inserisco in un array json
				objJson = (JSONObject) parserJson.parse(line); 
				executor.execute(new Task(objJson));
			}
		} catch (IOException e) {
			System.out.println("I/O error");
			e.printStackTrace();
			//System.exit(1);
		} catch(ParseException e){
			System.out.println("parse error");
			e.printStackTrace();
			//System.exit(1);
		}
	
		executor.shutdown();		
	}

}
