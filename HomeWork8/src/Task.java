
import java.io.*;
import java.net.*;

import org.json.simple.JSONObject;

public class Task implements Runnable{
	JSONObject immagine;
	
	
	public Task(JSONObject imm){
		immagine = imm;
	}
	
	public void run(){
		String line;
		URL url = null;
		URLConnection connection = null;

		line = immagine.get("media").toString();
		line = line.substring(6, line.length()-2);
		line = line.replace("\\/", "/");
		
		try {
			url = new URL(line);
			connection = url.openConnection();
			connection.connect();	
		} catch (MalformedURLException e) {
			System.out.println("url malformattato");
			e.printStackTrace();
			//System.exit(1);
		} catch(IOException e){
			System.out.println("I/O error");
			e.printStackTrace();
			//System.exit(1);
		}
		
		//read
		BufferedInputStream reader = null;
		BufferedOutputStream fout = null;
		try {
			reader = new BufferedInputStream(connection.getInputStream());
			fout = new BufferedOutputStream(new FileOutputStream(immagine.get("title").toString()));
			byte data[] = new byte[1024];
			int count;
			while((count = reader.read(data, 0, 1024)) != -1){
				fout.write(data, 0, count);
				fout.flush();
			}
		} catch (IOException e) {
			System.out.println("I/O errr in reader");
			e.printStackTrace();
		} finally{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(fout != null)
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
}