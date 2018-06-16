import java.util.Random;

public class Tesista extends Utente implements Runnable{

	private int id;
	private Laboratorio lab;
	
	public Tesista(int i, Laboratorio l){
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
			while(lab.getPc()[id % lab.getnComputer()] == true || lab.getProfessoriInAttesa() > 0){
				System.out.println("tesista " + id + " waiting");
				try { lab.wait(); } catch (InterruptedException e) { e.printStackTrace();}
			}
			// tesista entrato
			lab.getPc()[id % lab.getnComputer()] = true;
			lab.incInUso();;
		}
	}

	@Override
	public void doStuff(Random r) {
		try {
		//	System.out.println("tesista "+id+" è in laboratorio");
			Thread.sleep(r.nextInt(2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void esciLab(Laboratorio lab) {
		synchronized(lab){
			lab.getPc()[id % lab.getnComputer()] = false;
			lab.decInUso();
			lab.notifyAll();
		}
		
	}

}
