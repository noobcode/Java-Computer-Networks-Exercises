
public class Laboratorio {
	private int nComputer; // computer totali
	private int inUso;     // computer in uso
	private Boolean[] pc;
	private int professoriInAttesa = 0;
	
	public Laboratorio(int nComputer){
		this.nComputer = nComputer;
		inUso = 0;
		pc = new Boolean[nComputer];
		for(int i = 0; i < pc.length; i++) pc[i] = false;
	}

	public int getnComputer() {
		return nComputer;
	}

	public int getInUso() {
		return inUso;
	}

	public Boolean[] getPc() {
		return pc;
	}

	public int getProfessoriInAttesa() {
		return professoriInAttesa;
	}
	
	public void decInUso(){
		inUso--;
	}
	
	public void incInUso(){
		inUso++;
	}
	
	public void incProfWaiting(){
		professoriInAttesa++;
	}
	
	public void decProfWaiting(){
		professoriInAttesa--;
	}
	
	public void setInUso(int n){
		inUso = n;
	}
	
}
