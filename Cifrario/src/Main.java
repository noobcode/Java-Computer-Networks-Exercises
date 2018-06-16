import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException{
		
		/* codifico un file */
		Encryptor e = new Encryptor(Integer.parseInt(args[0]));
		try{
			e.encryptFile(args[1], args[2]);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		/* stampo due caratteri */
		char a = '\u64e5';
		Character letter = new Character('\u00F6');
		System.out.println(a);
		System.out.println(letter);
		
		/* UTF8 e UTF16 */
		byte[] Sbytes = null;
		int ndx;
		String sInitial = /*"Enc" + */"\u4E94";
		Sbytes = sInitial.getBytes("UTF-16");
		System.out.println(sInitial + "String as UTF-16, " + "Sbytes length: " + Sbytes.length);
		for(ndx = 0; ndx < Sbytes.length; ndx++ ){
			System.out.print(Integer.toHexString(Sbytes[ndx]) + " ");
		}
		
		System.out.println(" ");
		OutputStreamWriter osw = new OutputStreamWriter(System.out);
		String enc = osw.getEncoding();
		Sbytes = sInitial.getBytes();
		System.out.println(sInitial + "String as platform dafault " + enc + ", Sbytes length: " + Sbytes.length);
		for(ndx = 0; ndx < Sbytes.length; ndx++){
			System.out.print(Integer.toHexString(Sbytes[ndx]) + " ");
		}
		
		System.out.println(" ");
		Sbytes = sInitial.getBytes("UTF-8");
		System.out.println(sInitial + "String as UTF-8, sBytes lenght: " + Sbytes.length);
		for(ndx = 0; ndx < Sbytes.length; ndx++){
			System.out.print(Integer.toHexString(Sbytes[ndx]) + " ");
		}
		
		/* BufferedReader */
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
			while( (line = br.readLine()) != null)
				System.out.println(line);
		} catch (IOException e1) {
			System.out.println("I/O error " + e1.getMessage() + " " + e1.getCause() );
		}finally{
			if(br != null) { try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} }
		}
		
		
		
	}

}
