
public class Task implements Runnable{

	private String name;
	
	public Task(String name){
		this.name = name;
	}
	
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " : task " + name);
		
		try{
			Long duration = (long) (Math.random() * 10);
			System.out.println(Thread.currentThread().getName() + ": task " + name + ": doing a task during " + duration + " seconds");
			Thread.sleep(duration);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.println(Thread.currentThread().getName() + ": task finished " + name);
	}

}
