import java.util.*;

/* thread che effettua la lettura da tastiera */
public class ThreadReader extends Thread {
	private ArrayList<String> msgQueue;
	
	public ThreadReader(List<String> l, int ID){
		msgQueue = (ArrayList<String>)l;
	}
	
	public void run(){
		String msg;
		Scanner s = new Scanner(System.in);
		System.out.println("scrivi un messaggio oppure EXIT per uscire...");
		
		msg = s.nextLine();
		// esce dal ciclo quando il messaggio è EXIT
		while(true){
			// aggiungo il messaggio in coda
			synchronized(msgQueue){
				msgQueue.add(msg);
				msgQueue.notify();
			}
			if(msg.equals("EXIT")) break;
			msg = s.nextLine();
		}
		s.close();
	}
	
}
