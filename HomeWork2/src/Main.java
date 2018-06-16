import java.util.*;

public class Main {

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
