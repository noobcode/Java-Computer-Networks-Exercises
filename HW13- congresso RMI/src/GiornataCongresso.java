
public class GiornataCongresso {
	private int numeroSessioni = 12;
	private int numeroInterventi = 5;
	private String[][] giornata = new String[numeroSessioni][numeroInterventi]; 
	
	public GiornataCongresso(){
		for(int i = 0; i < numeroSessioni; i++)
			for(int j = 0; j < numeroInterventi; j++)
				giornata[i][j] = "NA";
	}

	public int getNumeroSessioni() {
		return numeroSessioni;
	}

	public int getNumeroInterventi() {
		return numeroInterventi;
	}

	public String[][] getGiornata() {
		return giornata;
	}

	public void setGiornata(int sessione, int intervento, String speaker) {
		giornata[sessione][intervento] = speaker;
	}
	
	public String toString(){
		String res = "";
		for(int i = 0; i < numeroSessioni; i++){
			for(int j = 0; j < numeroInterventi; j++){
				res = res + giornata[i][j];
				res = res + '\t';
			}
			res = res + '\n';
		}
		return res;
	}
	
	
}
