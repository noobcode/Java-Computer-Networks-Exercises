
public class Main {

	public static void main(String[] args) {
		for(int i = 1; i <= 10; i++){
			Calculator calc = new Calculator(i);
			Thread thread = new Thread(calc);
			thread.start();
		}
		System.out.println("avviato calcolo tabelline");
	}

}
