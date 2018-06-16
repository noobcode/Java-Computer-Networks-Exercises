import java.io.*;
import java.util.Date;

public class BufferDiff {

	public static void main(String[] args) throws IOException {
		/* buffered vs unbuffered stream */
		FileOutputStream unbufStream; // for writing streams of raw bytes
		BufferedOutputStream bufStream; // write bytes to the underlying stream without call the system for each byte written
		
		unbufStream = new FileOutputStream("test.one");
		bufStream = new BufferedOutputStream(new FileOutputStream("test.two"));
		
		System.out.println("write file unbuffered: " + time(unbufStream) + " ms");
		System.out.println("write file buffered: " + time(bufStream) + "  ms");
		
		/* tokenizerExample */
		FileReader fr = new FileReader(args[0]);
		StreamTokenizer fileTokenizer = new StreamTokenizer(fr);
		while(fileTokenizer.nextToken() != StreamTokenizer.TT_EOF){
			if(fileTokenizer.ttype == StreamTokenizer.TT_NUMBER){
				System.out.println(fileTokenizer.nval);
			} else if(fileTokenizer.ttype == StreamTokenizer.TT_WORD){
				System.out.println(fileTokenizer.sval);
			}
		}

		/* quoted tokenizer */
		BufferedReader myIn = new BufferedReader(new FileReader(args[0]));
		StreamTokenizer st = new StreamTokenizer(myIn);
		st.quoteChar('$');
		while(st.nextToken() != StreamTokenizer.TT_EOF){
			switch(st.ttype){
				case StreamTokenizer.TT_NUMBER:
					System.out.println("number: " + st.nval);
					break;
				case StreamTokenizer.TT_WORD:
					System.out.println("world: " + st.sval);
					break;
				case StreamTokenizer.TT_EOL:
					System.out.println("end of line");
					break;
				default:
					if((char)st.ttype == '$'){ // stampa la parte tra i dollari
						System.out.println("quoted: " + st.sval);
					}else{
						System.out.println("token: " + (char)st.ttype);
					}
			}
		}
		
		
		
		
		
		
		
		
		
	}
	
	static int time(OutputStream os) throws IOException{
		Date then = new Date();
		for(int i = 0; i < 50000; i++)
			os.write(1);
		os.close();
		return (int)((new Date().getTime()) - then.getTime());
	}

}
