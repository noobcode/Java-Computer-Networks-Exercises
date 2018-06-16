import java.util.Scanner;

public class HelloWorld {

	public static void main(String[] args) {
		// hello world
		System.out.println("hello world");
		
		// chiede in input due numeri
		Scanner scan = new Scanner(System.in);
		try{
			System.out.println("enter a number:");
			int x = scan.nextInt();
			System.out.println("second number:");
			int y = scan.nextInt();
			System.out.println("the sum is " + (x+y));
		}catch (Exception e){
			System.out.println("errore, devi inserire un numero" + e.getMessage());
			scan.close();
			
		}
		
		// gestione delle interruzioni
		SleepInterrupt si = new SleepInterrupt();
		Thread t = new Thread(si);
		t.start();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException x){ }
		System.out.println("interrompo l'altro thread");
		t.interrupt();
		System.out.println("sto terminando");
	}

}
  