import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class ServerChat {
	private static String multicastGroup = "224.0.0.19";
	private static int portGroup = 5000;
	private ServerSocketChannel channel;
	private Selector selector;
	private SelectionKey key;
	private DatagramChannel sockMulticast;
	
	public static void main(String[] args) {
		new ServerChat().startListening();
	}
	
	private ServerChat(){
		try {
			selector = Selector.open();
			channel = ServerSocketChannel.open();
			channel.configureBlocking(false);
			channel.bind(new InetSocketAddress("localhost",1337));
			key = channel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("server inizializzato");
			System.out.println("channel: " + channel.getLocalAddress().toString());
			System.out.println("selector: " + selector.toString());
			System.out.println("keys: " + key.interestOps() + " selector associato: " + key.selector());
			
			// create a new channel
			sockMulticast = DatagramChannel.open(StandardProtocolFamily.INET);
			InetAddress group = InetAddress.getByName(multicastGroup);
			// check if the group is multicast
			if(group.isMulticastAddress()){
				// check if the channel was successfully created
				if(sockMulticast.isOpen()){
					// set some options
					sockMulticast.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				} else {
					System.out.println("the channel cannot be opened");
				}
			} else {
				System.out.println("the channel is not multicast address");
			}
			sockMulticast.configureBlocking(false);
			sockMulticast.connect(new InetSocketAddress(multicastGroup, portGroup));
			sockMulticast.register(selector, SelectionKey.OP_WRITE);
			System.out.println("-------------------------");
			System.out.println("multicast group connected ");
			System.out.println("local address:" + sockMulticast.getLocalAddress());
			System.out.println("remote: " +  sockMulticast.getRemoteAddress());
			System.out.println("supported options: " + sockMulticast.supportedOptions());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startListening(){
		System.out.println("server is listening on:");
		System.out.println(channel.socket().getInetAddress().getHostAddress());
		System.out.println(channel.socket().getLocalPort());
		
		ByteBuffer in = ByteBuffer.allocate(512);
		ByteBuffer out = ByteBuffer.allocate(512);
		while(true){
			try {
				selector.select(); // si blocca finché non c'è un evento sulla socket
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				handleRequests(selectedKeys, in, out);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleRequests(Set<SelectionKey> selectedKeys, ByteBuffer in, ByteBuffer out) throws IOException{		
		boolean messaggioDaScrivere = false;
		for(SelectionKey k: selectedKeys){
			// a client has asked for a new connection
			if(k.isAcceptable()){
				SocketChannel client = channel.accept();
				System.out.println("---------------------------");
				System.out.println("connessione tcp accettata: ");
				System.out.println("local address: " + client.getLocalAddress());
				System.out.println("remote address: " + client.getRemoteAddress());
				System.out.println("---------------------------");
				client.configureBlocking(false);
				// registra il client con lo stesso selector in cui è registrato il server
				client.register(selector, SelectionKey.OP_READ);
				continue;
			} 
			// a client has sent something to be read
			else if(k.isReadable()){
				SocketChannel client = (SocketChannel) k.channel();
				client.read(in); 
				in.flip();
				byte[] msg = new byte[in.limit()];
				in.get(msg);
				System.out.println("ricevuto : " + new String(msg));
				in.clear();
				//metto i dati nel buffer di output
				out.flip();
				out = ByteBuffer.wrap(msg);
				messaggioDaScrivere = true;
			}
			else if(k.isWritable()){
				if(messaggioDaScrivere){
				// invio il messaggio sul gruppo di multicast
				System.out.println("writable");
				sockMulticast.write(out);
				sockMulticast.send(out, new InetSocketAddress(multicastGroup,portGroup));
				out.clear();
				System.out.println("spedito");
				messaggioDaScrivere = false;
				}
			}
		}
		selectedKeys.clear();
	}
}


