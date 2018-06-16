import java.util.Random;

public class Studente extends Utente implements Runnable{

	private int id;
	private Laboratorio lab;
	private int place;
	
	public Studente(int i, Laboratorio l){
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

	public void accediLab(Laboratorio lab){
		synchronized(lab){
			// attendi
			while(lab.getInUso() == lab.getnComputer() || lab.getProfessoriInAttesa() > 0){ 
				System.out.println("studente " + id + " waiting");
				try { lab.wait(); } catch (InterruptedException e) {e.printStackTrace();}
			}
			//studente entrato
			place = findPlace(lab.getPc()); 
			lab.getPc()[place] = true;
			lab.incInUso();
		}
	}
	
	public void doStuff(Random r){
		try {
		//	System.out.println("studente "+id+" è in laboratorio");
			Thread.sleep(r.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void esciLab(Laboratorio lab){
		synchronized(lab){
			lab.getPc()[place] = false;
			lab.decInUso();
			lab.notifyAll();
		}
	}
	
	private static int findPlace(Boolean[] pc){
		int j = 0;
		while(j < pc.length){
			if(pc[j] == false) break;
			else j++;
		}
		if(j < pc.length) return j;
		else return -1;
	}
	
}
