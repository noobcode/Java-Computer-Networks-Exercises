import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class SelectSockets {
	ServerSocketChannel serverSocketChannel;
	Selector selector;
	
	public SelectSockets(){
		try{
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			InetSocketAddress add = new InetSocketAddress(InetAddress.getLocalHost(), 4550);
			serverSocketChannel.bind(add);
			SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("serverSocketChannel's registered key is : " + key.channel().toString());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void startListening(){
		System.out.println("server is listening on: " + serverSocketChannel.socket().getInetAddress().getHostAddress() + ":" + serverSocketChannel.socket().getLocalPort());
		while(true){
			try{
				// this line blocks until some events has occurred in the underlying socket
				selector.select();
				// get the selected keys set
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				
				while(iterator.hasNext()){
					SelectionKey key = (SelectionKey) iterator.next();
					iterator.remove();
					
					// if a client has asked for a new connection
					if(key.isAcceptable()){
						System.out.println("key ready to perform accept() : " + key.channel().toString());
						SocketChannel client = serverSocketChannel.accept();
						client.configureBlocking(false);
						//register the client socket with the same selector to which we have registered the serverSocketChannel
						client.register(selector, SelectionKey.OP_READ);
						continue;
					}
					// if the client has sent something to be read by this server
					if(key.isReadable()){
						System.out.println("key ready to perform read(): " + key.channel().toString());
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer bb = ByteBuffer.allocate(1024);
						// read the message sent by the client
						client.read(bb);
						bb.flip();
						byte[] array = new byte[bb.limit()];
						bb.get(array);
						System.out.println(new String(array));
						continue;
					}
					
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
}
