import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostLookUp {

	public static void main(String[] args) {
		if(args.length > 0){
			for(int i = 0; i < args.length; i++){
				System.out.println(lookup(args[i]));
			}
		} else {
			System.out.println("modalità interattiva non attivata");
		}

	}
	
	private static String lookup(String host){
		InetAddress node;
		try {
			node = InetAddress.getByName(host);
			if(isHostName(host)){
				return node.getHostAddress();
			} else {
				return node.getHostName();
			}
		} catch (UnknownHostException e) {
			return ("non ho trovato l'host " + host);
		}
	}	
		private static boolean isHostName(String host){
			char[] ca = host.toCharArray();
			for(int i = 0; i < ca.length; i++){
				if(!Character.isDigit(ca[i])){
					if(ca[i] != '.'){
						return true;
					}
				}
			}
			return false;
		}
		
	
	

}
