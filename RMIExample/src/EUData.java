/*definisce oggetti che descrivono la singola nazione*/

public class EUData {
	private String language;
	private int population;
	private String capital;
	
	public EUData(String l, int p, String c){
	language = l;
	population = p;
	capital = c;
	}

	public String getLanguage() {
		return language;
	}

	public int getPopulation() {
		return population;
	}

	public String getCapital() {
		return capital;
	}	
	
	public void setCapital(String name){
		capital = name;
	}
	
}
