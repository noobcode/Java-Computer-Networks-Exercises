import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class Task implements Callable <InetAddress>{
	String hostName;
	
	public Task(String hn){
		hostName = hn; 
	}

	@Override
	public InetAddress call() {
		try {
			InetAddress node = InetAddress.getByName(hostName);
			return node;
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
	
}
