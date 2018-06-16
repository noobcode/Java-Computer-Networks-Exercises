import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		CarList list;
		
		list = new CarList(args[0]);
		
		Car c1 = new Car("bmw", 2010, 10000);
		Car c2 = new Car("punto", 1990, 20000, 2000);
		Car c3 = new Car("lancia", 1982, 1000);
		Car c4 = new Car("ferrari", 1990, 20000, 450000);
		Car c5 = new Car("panda", 1980, 150000, 500);

		
		list.addCar(c1);
		list.addCar(c2);
		list.addCar(c3);
		list.addCar(c4);
		list.addCar(c5);
		
		System.out.println("autonuove: " + list.countNewCars());
		System.out.println("l'auto più costosa è " + list.mostExpensiveCar().getModel());
		list.Remove(c4);
	}

}
