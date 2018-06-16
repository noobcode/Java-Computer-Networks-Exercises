import java.util.concurrent.*;

public class Server {
	private ThreadPoolExecutor executor;
	
	public Server(){
		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	public void executeTask(Task task){
		System.out.println("a new task has arrived");
		executor.execute(task);
		System.out.println("Server: Pool size: " + executor.getPoolSize());
		System.out.println("Server: active count: " + executor.getActiveCount());
		System.out.println("Server: completed tasks: " + executor.getCompletedTaskCount());
		
	}
	
	public void endServer(){
		executor.shutdown();
	}
	
}
