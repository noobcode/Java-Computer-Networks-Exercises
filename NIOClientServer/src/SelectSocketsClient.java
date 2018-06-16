import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;


public class SelectSocketsClient {
	String myIdentity;
	
	public SelectSocketsClient(String id){
		myIdentity = id;
	}
	
	void talkToServer(){
		try{
			SocketChannel mySocket = SocketChannel.open();
			mySocket.configureBlocking(false);
			mySocket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 4550));
			Selector selector = Selector.open();
			mySocket.register(selector, SelectionKey.OP_CONNECT);
			while(selector.select() > 0){ 
				// select() blocks until something happens on the underlying socket
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				
				while(it.hasNext()){
					SelectionKey key = (SelectionKey) (it.next());
					SocketChannel myChannel = (SocketChannel) key.channel(); // returns the channel for which the key was created
					it.remove();
					if(key.isConnectable()){
						if(myChannel.isConnectionPending()){ 
							myChannel.finishConnect();
							System.out.println("connection was pending but now is finished connecting.");
						}
					}
					ByteBuffer bb = null;
					for(int i = 0; i < 10; i ++){
						System.out.println("spedisco al server");
						bb = ByteBuffer.wrap(new String("i am client : " + myIdentity).getBytes());
						myChannel.write(bb);
						bb.clear();
						Thread.sleep(1000);
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
