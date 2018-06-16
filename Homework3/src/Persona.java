
public class Persona implements Runnable{

	private String name;
	
	public Persona(String name){
		this.name = name;
	}
	
	@Override
	public void run() {
		int workTime;
		
		workTime = (int)(Math.random()*10);
		System.out.println(name + " attivata per " + workTime  + " ms");
		try {
			Thread.sleep(workTime*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + " esce dall'ufficio");
	}
	
}
