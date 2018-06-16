import java.util.*;
import java.io.*;

public class WritingSize {

	public static void main(String[] args) throws IOException {
		test(new ArrayList<String>());
		test(new LinkedList<String>());
		test(new Vector<String>());

	}
	
	private static void insertJunk(List<String> list){
		for(int i = 0; i< 100000; i++)
			list.add("junk");
		list.clear(); // perché usa clear() ?
	}

	public static void test(List<String> list) throws IOException{
		insertJunk(list);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(list);
		out.close();
		System.out.println(list.getClass().getSimpleName() + " used " + baos.toByteArray().length + " bytes");
		
	}
}
