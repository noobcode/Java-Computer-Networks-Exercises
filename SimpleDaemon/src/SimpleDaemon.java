
public class SimpleDaemon implements Runnable{
	public SimpleDaemon(){}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e){
				throw new RuntimeException(e);
			}
			System.out.println("termino");
		}
	}
}
