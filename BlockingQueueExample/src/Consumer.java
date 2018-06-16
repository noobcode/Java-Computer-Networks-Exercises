import java.util.concurrent.*;

public class Consumer extends Thread{
	
	protected BlockingQueue<String> queue = null;
	
	public Consumer(BlockingQueue<String> q){
		queue = q;
	}
	
	public void run(){
		try{
			System.out.println(queue.take());
			System.out.println(queue.take());
			System.out.println(queue.take());
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	
}
