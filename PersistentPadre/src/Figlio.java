import java.io.Serializable;

public class Figlio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nome;
	private String cognome;
	
	public Figlio(String n,String c){
		nome = n;
		cognome = c;
	}
	
	public String toString(){
		return "nome: " + nome + " cognome: " + cognome;
	}
	
}
