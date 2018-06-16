
public class Main {

	public static void main(String[] args) {
		for(int i = 0; i <= 10; i++){
			SimpleDaemon sd = new SimpleDaemon();
			Thread t = new Thread(sd);
			t.setDaemon(true);
			t.start();
		}
		// il main termina subito e il processo termina (gli altri thread sono daemon) 
		// quindi non viene stampato nulla

	}

}
