import java.util.Scanner;
import java.util.Stack;


public class Clock {
	
	private static Scanner s;

	public static void main(String[] args) {
		String x = "";
		Stack<ClockTh> stack = new Stack<ClockTh>(); // thread gestiti in LIFO
		ClockTh popped; // thread estratto dalla cima dello stack
		
		s = new Scanner(System.in);
		
		do{
			x = s.nextLine();
			
			switch(x){
				case "1":
				case "2":
				case "3":
					ClockTh clock = new ClockTh(Integer.parseInt(x));
					//Thread t = new Thread(clock);
					clock.start();
					stack.push(clock);
					break;
				case "k":
					// terminazione immediata,
					// se era in sleep, si sveglia e termina
					popped = stack.pop();
					popped.interrupt();
					break;
				case "q":
					// terminano la sleep, stampano e terminano
					while(!stack.isEmpty()){
						popped = stack.pop();
						popped.stopClock();
					}
					break;
			}
		} while (!x.equals("q") && !stack.isEmpty());
			
		System.out.println("main termina");
	}
}

