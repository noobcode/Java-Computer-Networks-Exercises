import java.util.*;

public class HomeWork2 {

	public static void main(String[] args) {
		double accuracy;
		long timeToWait;
		PiGreco pigreco;
		Thread piGrecoThread;
		Scanner scan = new  Scanner(System.in);
		
		try{
			accuracy = scan.nextDouble();
			timeToWait = scan.nextLong();
		} catch(InputMismatchException e){
			System.out.println("errore scan " + e.getMessage());
			scan.close();
			return;
		}
		
		pigreco = new PiGreco(accuracy);
		piGrecoThread = new Thread(pigreco);
		piGrecoThread.start();
		
		if(piGrecoThread.isAlive()){
			synchronized(piGrecoThread){
				try {
					piGrecoThread.wait(timeToWait*1000);
					piGrecoThread.interrupt();
				} catch (InterruptedException e) {
					System.out.println("errore sleep " + e.getMessage());
					scan.close();
					return;
				}
			}
		
			try {
				piGrecoThread.join();
			} catch (InterruptedException e) {
				System.out.println("errore join " + e.getMessage());
				scan.close();
				return;
			}
		}
		scan.close();
		System.out.println("main termina");
	}

}

/*class PiGreco implements Runnable{
	
	private double accuracy;
	
	public PiGreco(double accuracy){
		this.accuracy = accuracy;
	}
	
	public void run(){
		double approx = 4.0;
		long i = 3;
		boolean flag = false;
		
		synchronized(this){
			while( Math.abs((approx - Math.PI)) >= accuracy ){
				if(flag == true){
					approx = approx + ((double)4/i);
				} else {
					approx = approx - ((double)4/i);
					}
				flag = !flag;
				i += 2;			
				if(Thread.currentThread().isInterrupted())
				break;
			}
			notify();
		}
		
		System.out.println("PI GRECO = " + Math.PI + "\napprox =   " + approx);
		System.out.println("thread termina");
	}
}*/

