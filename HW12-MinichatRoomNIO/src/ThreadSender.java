import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

/* effettua la lettura dal gruppo di multicast e invia i messaggi al server */
public class ThreadSender extends Thread {
	private ArrayList<String> msgQueue;
	private SocketChannel channel;
	private DatagramChannel multicastChannel;
	private Selector selector;
	private int clientID;
	
	public ThreadSender(List<String> l, SocketChannel sock, DatagramChannel dc, Selector s, int id){
		msgQueue = (ArrayList<String>)l;
		channel = sock;
		multicastChannel = dc;
		selector = s;
		clientID = id;
	}
	
	public void run(){
		String msg = null;
		ByteBuffer bb = ByteBuffer.allocate(512);
		while(true){
			// se c'è un messaggio in coda lo estraggo...
			msg  = retrieveMsg();
			try{
				if(selector.select() == 0) { System.out.println("== 0"); continue; }
				else {
					Set<SelectionKey> keys = selector.selectedKeys();
					handleKeys(keys, msg, bb);
				}
			}catch(IOException e){
					e.printStackTrace();
				}
			}
	}	
	
	private void handleKeys(Set<SelectionKey> keys, String msg, ByteBuffer bb) throws IOException{
		for(SelectionKey k : keys){
			if(k.channel() == multicastChannel || k.channel().equals(multicastChannel))	{
				// 	NON LO STAMPA MAI
				System.out.println("selezionato multicastChannel");
			}
			else if(k.isConnectable()){
				if(channel.isConnectionPending()){
					channel.finishConnect();
					System.out.println("connesso TCP");
				}
			} else if(msg !=null && k.isWritable()){
				System.out.println("invio messaggio");
				// GESTIRE EXIT E FORMATO MESSAGGIO
				bb.flip();
				bb = ByteBuffer.wrap(msg.getBytes());
				channel.write(bb);
				bb.clear();
			} else if(k.isReadable() || k.channel() == multicastChannel){
				System.out.println("è readable");
				bb.clear();
				multicastChannel.receive(bb);
				System.out.println(new String(bb.array()));
			}
		}
		keys.clear();
	}
	
	private String retrieveMsg(){
		synchronized(msgQueue){
			if(!msgQueue.isEmpty()){
				return msgQueue.remove(0);
			}
		}
		return null;
	}
	
}