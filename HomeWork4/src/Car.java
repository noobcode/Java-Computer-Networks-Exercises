
public class Car {
	private String model;
	private int annoManifattura;
	private double km;
	private int price;
	
	public Car(String m, int a, int p){
		model = m;
		annoManifattura = a;
		km = 0.0;
		price = p;
	}
	
	public Car(String m, int a, double km, int price){
		model = m;
		annoManifattura = a;
		this.km = km;
		this.price = price;
	}
	
	public String getModel(){
		return model;
	}
	
	public int getYear(){
		return annoManifattura;
	}
	
	public double getKilometers(){
		return km;
	}
	
	public int getPrice(){
		return price;
	}
	
	public String toString(){
		return (getModel() + " " + getYear() + " " + getKilometers() + " "  +  getPrice());
	}
	
	public boolean equalTo(Car c){
		return this.equals(c);
	}
	
}
