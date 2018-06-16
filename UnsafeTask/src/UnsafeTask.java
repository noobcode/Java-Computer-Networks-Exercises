import java.util.Date;

public class UnsafeTask implements Runnable{
	private Date startDate;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		startDate = new Date();
		System.out.println("starting thread" + Thread.currentThread().getId() + " " + startDate);
		
		try{
			Thread.sleep(8000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("finished thread" + Thread.currentThread().getId() + " " + startDate);
	}
	
	
}
