
public class SleepInterrupt implements Runnable{
	
	public void run(){
		
		try{
			System.out.println("dormo per 5 secondi");
			Thread.sleep(5000);
			System.out.println("svegliato");
		}catch(InterruptedException x){
			System.out.println("interrotto: " + x.getCause());
			return;
		}
		System.out.println("esco normalmente");
	}
}
