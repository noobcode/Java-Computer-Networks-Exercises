import java.util.Date;

public class SafeTask implements Runnable{
	private static ThreadLocal<Date> startDate = new ThreadLocal<Date>(){
		protected Date initialValue() {
			return new Date();
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("starting thread" + Thread.currentThread().getId() + " " + startDate.get());
		
		try{
			Thread.sleep(8000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("finished thread" + Thread.currentThread().getId() + " " + startDate.get());
	}
	
	
}
