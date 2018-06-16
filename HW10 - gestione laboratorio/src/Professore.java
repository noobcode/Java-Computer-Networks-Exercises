import java.util.Random;

public class Professore extends Utente implements Runnable{

	private int id;
	private Laboratorio lab;
	
	public Professore(int i, Laboratorio l){
		this.id = i;
		lab = l;
	}
	
	@Override
	public void run() {
		int numeroAccessi = r.nextInt(5);
		
		for(int i = 0; i < numeroAccessi; i++){
			accediLab(lab);
			doStuff(r);
			esciLab(lab);
			try {
				Thread.sleep(r.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void accediLab(Laboratorio lab) {
		synchronized(lab){
			// attendi
			lab.incProfWaiting();
			while(lab.getInUso() > 0){ 
				System.out.println("professore " + id + " waiting");
				try { lab.wait(); } catch (InterruptedException e) {e.printStackTrace();}
			}
			lab.decProfWaiting();
			lab.setInUso(lab.getnComputer());
			for(int i = 0; i < lab.getnComputer(); i++)	lab.getPc()[i] = true;
		}
		
	}

	@Override
	public void doStuff(Random r) {
		try {
			System.out.println("il professor " + id + " sta effettuando un test");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void esciLab(Laboratorio lab) {
		synchronized(lab){
			lab.setInUso(0);
			for(int i = 0; i < lab.getnComputer(); i++)	lab.getPc()[i] = false;
			lab.notifyAll();
			System.out.println("il professor " + id + " esce");
		}
		
	}

}
