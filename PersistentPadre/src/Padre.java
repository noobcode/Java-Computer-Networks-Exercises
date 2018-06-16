import java.io.*;
import java.util.*;

public class Padre implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nome;
	private String cognome;
	private Collection<Figlio> figli;
	//transient private Collection<Figlio> figli; // campo non serializzato
	
	public Padre(String n, String c){
		nome = n;
		cognome = c;
		figli = new ArrayList<Figlio>();
	}
	
	public void addFiglio(Figlio f){
		figli.add(f);
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder("padre");
		tmp.append("\n");
		tmp.append("nome: ");
		tmp.append(this.nome);
		tmp.append("\ncognome: ");
		tmp.append(this.cognome);
		tmp.append("\nFigli: ");
		tmp.append(figli.toString());
		return tmp.toString();
	}
	
}
