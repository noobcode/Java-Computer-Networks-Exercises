import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ClientChat {

	public static void main(String[] args) {
		InetAddress group = null;
		int portGroup = 0;
		int clientID = 0; 
		
		try{
			group = InetAddress.getByName(args[0]);
			portGroup = Integer.parseInt(args[1]);
			clientID = Integer.parseInt(args[2]);
		}catch(Exception e){
			System.out.println("usage: java ClientChat multicast_address port ClientID");
			System.exit(1);
		}
		
		String host = "localhost";
		int portTCP = 1337;
		SocketChannel channel = null;
		DatagramChannel multicastChannel = null;
		Selector selector = null;
		try {
			// si connette al server con una connessione TCP
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress(host,portTCP));
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_WRITE);
			
			
			InetSocketAddress remote = new InetSocketAddress(group, portGroup);
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName("localhost"));
           multicastChannel = DatagramChannel.open(StandardProtocolFamily.INET)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
           multicastChannel.socket().bind(new InetSocketAddress(0));
           multicastChannel.connect(remote);
           multicastChannel.configureBlocking(false);
           multicastChannel.join(group, ni);
		   
		    System.out.println("-- informazioni multicast --");
		    System.out.println("remote address: " + multicastChannel.getRemoteAddress());
		    System.out.println("local address: " + multicastChannel.getLocalAddress());
		    if(multicastChannel.isConnected()) System.out.println("connesso multicast");
		    if(multicastChannel.isRegistered()) System.out.println("registrato multicast");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// coda di messaggi condivisa
		ArrayList<String> msgQueue = new ArrayList<String>();
		ThreadReader reader = new ThreadReader(msgQueue, clientID);
		ThreadSender sender = new ThreadSender(msgQueue, channel, multicastChannel, selector, clientID);
		reader.start();
		sender.start();
		
		try{
			reader.join();
			sender.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
