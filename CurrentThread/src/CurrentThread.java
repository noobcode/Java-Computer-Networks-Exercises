import java.io.*;

public class CurrentThread {
	
	public static void main(String args[]) throws Exception{
		/*
		Thread current = Thread.currentThread();
		System.out.println("ID: " + current.getId());
		System.out.println("NOME: " + current.getName());
		System.out.println("PRIORITà: " + current.getPriority());
		System.out.println("NOME GRUPPO: " + current.getThreadGroup().getName());
		*/
		
		Thread threads[] = new Thread[10];
		Thread.State status[] = new Thread.State[10];
		for(int i = 0; i < 10; i++){
			threads[i] = new Thread(new Calculator(i));
			if((i%2) == 0){
				threads[i].setPriority(Thread.MAX_PRIORITY);
			} else {
				threads[i].setPriority(Thread.MIN_PRIORITY);
			}
			threads[i].setName("Thread "  + i);
		}
		
		FileWriter file = new FileWriter("log.txt");
		PrintWriter pw = new PrintWriter(file);
		pw.println("*****************");
		for(int i = 0; i < 10; i++){
			pw.println("status of thread" + i + ": " + threads[i].getState());
			status[i] = threads[i].getState();
		}
		
		for(int i = 0; i < 10; i++)
			threads[i].start();
		
		boolean finish = false;
		while(!finish){
			for(int i = 0; i < 10; i++){
				if(threads[i].getState() != status[i]){
					pw.printf("ID %d Name %s\n", threads[i].getId() , threads[i].getName());
					pw.printf("Old state %s -> New state %s", status[i], threads[i].getState());
					pw.printf("********\n");
					pw.flush();
					status[i] = threads[i].getState();
				}
				
			}
			finish = true;
			for(int i = 0; i < 10; i++)
				finish = finish && (threads[i].getState() == Thread.State.TERMINATED);
		}
		pw.close();
	}
	
}
