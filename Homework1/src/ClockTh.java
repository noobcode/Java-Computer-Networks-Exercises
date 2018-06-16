
public class ClockTh extends Thread{
	
	private int x;
	private boolean termina = false;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long startExecution = System.currentTimeMillis();;
		
		while(!termina){ 
			
			try {
				Thread.sleep(x*1000);
				System.out.println("thread-" + Thread.currentThread().getName() + " avviato da " + getTimeOfExec(startExecution)/1000 + " secondi");
			} catch (InterruptedException e) {
				System.out.println("thread-" + Thread.currentThread().getName() + " terminato a " + getTimeOfExec(startExecution) + " ms dall'avvio");
				return;
			}
			
		}
	}
	
	public ClockTh (int value){
		x = value; 
	}
	
	public static long getTimeOfExec(long startExecution){
		return (System.currentTimeMillis() - startExecution);
	}
	
	public void stopClock(){
		termina = true;
	}
	
}
