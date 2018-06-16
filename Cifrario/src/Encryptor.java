import java.io.*;

/*un Encryptor cifra i file usando il cifrario di Cesare.
 * per decifrare, utilizzare un encryptor la cui chiave è il 
 * negativo della chiave di cifratura. */

public class Encryptor {
	private int key;
	
	public Encryptor(int aKey){
		key = aKey;
	}
	
	public void encryptFile(String inFile, String outFile) throws IOException{
		InputStream in = null;
		OutputStream out = null;
		try{
			in = new FileInputStream(inFile);
			out = new FileOutputStream(outFile);
			encryptStream(in,out);
		} finally {
			if(in != null){ in.close();	}
			if(out != null){ out.flush(); out.close(); }
		}
	}
	
	/* nota: nel file criptato viene inserito in \n */
	public void encryptStream(InputStream in, OutputStream out) throws IOException{
		boolean done = false;
		while(!done){
			int next = in.read();
			if(next == -1) {
				done = true;
				break;
			}
			byte b = (byte)next;
			byte c = encrypt(b);
			out.write(c);
		}
	}
	
	/* cifra un byte */
	public byte encrypt(byte b){
		return (byte)(b + key);
	}
}
