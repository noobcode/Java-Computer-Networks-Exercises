import java.io.*;
import java.nio.channels.*;
import java.nio.*;

public class ChannelExample {

	public static void main(String[] args) {
		FileInputStream is = null;
		try{
			File aFile = new File("/home/carlo/Desktop/iia");
			is = new FileInputStream(aFile);
			FileChannel inChannel = is.getChannel();
			// create a buffer with capacity of 48 bytes
			ByteBuffer buf = ByteBuffer.allocate(48);
			// read into buffer
			int bytesRead = inChannel.read(buf);
			while(bytesRead != -1){
				buf.flip(); // make buffer ready for read
				while(buf.hasRemaining())
					System.out.print((char)buf.get()); // read 1 byte at a time
				System.out.println();
				buf.clear(); // make buffer ready for writing
				bytesRead = inChannel.read(buf);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(is != null) is.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}

}
