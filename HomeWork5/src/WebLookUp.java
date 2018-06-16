import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class WebLookUp {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		BufferedReader bReader;
		String line;
		ExecutorService pool = Executors.newCachedThreadPool();
		
		if(args.length == 1){
			bReader = new BufferedReader(new FileReader(args[0]));
		} else {
			System.out.println("immetti nome file");
			Scanner s = new Scanner(System.in);
			bReader = new BufferedReader(new FileReader(s.nextLine()));
			s.close();
		}
		
		Vector<Future<InetAddress>> v = new Vector<Future<InetAddress>>();
		while((line = bReader.readLine()) != null){
			Task t = new Task(line);
			Future<InetAddress> res = pool.submit(t); 
			v.addElement(res);
		}
		pool.shutdown();
		
		// stampo i valori degli indirizzi IP
		for(int i = 0; i < v.size(); i++)
			System.out.println(v.get(i).get().getHostAddress());
		
		
	}

}
