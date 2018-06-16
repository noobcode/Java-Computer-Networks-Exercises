
public class ServerMain {

	public static void main(String[] args) {
		SelectSockets server = new SelectSockets();
		server.startListening();
	}

}
