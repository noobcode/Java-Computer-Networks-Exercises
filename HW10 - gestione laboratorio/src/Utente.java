import java.util.Random;



public abstract class Utente {
	static Random r = new Random(System.currentTimeMillis());
	
	public abstract void accediLab(Laboratorio lab);
	public abstract void doStuff(Random r);
	public abstract void esciLab(Laboratorio lab);
}

