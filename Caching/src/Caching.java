import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.*;

public class Caching {
	// gli effetti del caching sulle prestazioni del programma
	// prova con CACHINGTIME = 0
	public static final String CACHINGTIME = "1000";
	
	public static void main(String[] args) {
		Security.setProperty("networkaddress.cache.ttl", CACHINGTIME);
		long time1 = System.currentTimeMillis();
		for(int i = 0; i < 1000; i++){
			try{
				System.out.println(InetAddress.getByName("www.cnn.com").getHostAddress());
			}catch(UnknownHostException uhe){
				System.out.println("UHE");
			}
		}
		long time2 = System.currentTimeMillis();
		long diff = time2 -time1;
		System.out.println("tempo trascorso è: " + diff);

	}

}
