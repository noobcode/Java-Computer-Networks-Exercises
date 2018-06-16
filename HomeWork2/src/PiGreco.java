
public class PiGreco implements Runnable{
	
	private double accuracy;
	
	public PiGreco(double accuracy){
		this.accuracy = accuracy;
	}
	
	public void run(){
		double approx = 4.0;
		long i = 3;
		boolean flag = false;
		
		// Gregory-Leibniz ( PiGreco= 4/1 - 4/3 + 4/5 - 4/7 + 4/9 - 4/11 ...)
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
}
