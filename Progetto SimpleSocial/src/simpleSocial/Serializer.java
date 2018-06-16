package simpleSocial;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer extends Thread{
	
	private SocialServer server;
	private boolean haveToWait;
	
	public Serializer(SocialServer s){
		server = s;
		haveToWait = true;
	}
	
	public void run(){
		
		while(true){
			
			synchronized(this){
				while(haveToWait)
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			// serializza
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(new FileOutputStream("server.dat"));
				oos.writeObject(server);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			haveToWait = true;
		}
		
	}
	
	public void serialize(){
		haveToWait = false;
	}
	
}
