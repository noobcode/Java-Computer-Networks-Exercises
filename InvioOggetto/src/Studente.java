import java.io.*;

public class Studente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int matricola;
	private String nome;
	private String cognome;
	private String cdl;
	
	public Studente(int m, String n, String c, String cdl){
		matricola = m;
		nome = n;
		cognome = c;
		this.cdl = cdl;
	}
	
	public int getMatricola(){return matricola;}
	public String getNome(){return nome;}
	public String getCognome(){return cognome;}
	public String getCorsoDiLaurea(){return cdl;}
	
	public String toString(){
		return matricola + " " + nome + " " + cognome + " " + cdl;
	}
	
}
