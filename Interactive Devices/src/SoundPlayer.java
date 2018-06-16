import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

public class SoundPlayer extends Thread { 
	
	String name;
	
	public SoundPlayer(String name){
		this.name = name;
	}
	
	public void run(){
	File f = new File(this.name);
    // Create a Player object that realizes the audio
    Player p = null	;
	try {	
		p = Manager.createRealizedPlayer(f.toURI().toURL());
	} catch (NoPlayerException | CannotRealizeException | IOException e) {
		e.printStackTrace();
	}
    p.start();
	}
}
