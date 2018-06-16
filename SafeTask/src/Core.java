
public class Core {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SafeTask task = new SafeTask();
		for(int i = 0; i < 3; i++){
			Thread thread = new Thread(task);
			thread.start();
		
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}