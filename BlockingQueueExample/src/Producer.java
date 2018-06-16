import java.util.concurrent.*;

public class Producer extends Thread {
	
	protected BlockingQueue<String> queue = null;
	
	
	public Producer(BlockingQueue<String> q){
		queue = q;
	}
	
	public void run(){
		try{
			queue.put("1");
			Thread.sleep(1000);
			queue.put("2");
			Thread.sleep(1000);
			queue.put("3");
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
}