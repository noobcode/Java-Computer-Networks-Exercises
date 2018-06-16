import java.util.concurrent.*;

public class Ufficio extends Thread{

	private ThreadPoolExecutor executor; 	// sportelli e seconda sala d'attesa
	private BlockingQueue<Persona> queue;  // prima sala d'attesa
	private boolean isOpen;
	
	public Ufficio(int nPersone, int nSportelli, int k){
		executor = new ThreadPoolExecutor(nSportelli, nSportelli , 3, TimeUnit.SECONDS,  new ArrayBlockingQueue<Runnable>(k));
		queue = new ArrayBlockingQueue<Persona>(nPersone);
		isOpen = true;
	}
	
	public void run(){
		while(isOpen){
			if(executor.getQueue().remainingCapacity() > 0){
				try {
					// prende una persona dalla prima sala d'attesa e la mette nella seconda
					executor.execute(queue.take());	
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
		}
		
		// svuota la prima sala d'attesa
		System.out.println("l'ufficio sta chiudendo, servo comunque i clienti nella prima sala d'attesa");
		while(!queue.isEmpty()){
			try {
				if(executor.getQueue().remainingCapacity() > 0){
					executor.execute(queue.take());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// non vengono accettati altri task
		executor.shutdown();
	}
	
	public void putClientInWaitingRoom(Persona p) {
		try{
			queue.put(p);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void closeOffice(){
		isOpen = false;
	}
}
