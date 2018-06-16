import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

public class CarList {
	private String fileName;
	
	public CarList(String str){
		fileName = str;
	}
	
	public int countNewCars() throws IOException{
		int count = 0;
		StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader(fileName)));
		while(st.nextToken() != StreamTokenizer.TT_EOF){
			if(st.ttype == StreamTokenizer.TT_NUMBER){
				if(st.nval == 0.0){
					count++;
				}
			}
		}
		return count;
	}
	
	void addCar(Car c) throws IOException{
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fileName, true));
		buffWriter.append(c.toString());
		buffWriter.newLine();
		buffWriter.flush();
		buffWriter.close();
	}
	
	void Remove(Car c) throws IOException{
		BufferedReader buffReader = new BufferedReader(new FileReader(fileName));
		BufferedWriter buffWriter;
		String fileString = "";
		String line;
		
		while((line = buffReader.readLine()) != null){
			if(!line.equals(c.toString())){
				// inserisco nella stringa tutte le macchine tranne quella che vogliamo cancellare
				fileString = fileString + line + "\n";
			}
		}
		// sovrascrivo il vecchio file
		buffWriter = new BufferedWriter(new FileWriter(fileName));
		buffWriter.write(fileString.toString());
		buffWriter.flush();
		
		buffReader.close();
		buffWriter.close();
	}
	
	Car mostExpensiveCar() throws IOException{
		String carMax = "";
		BufferedReader buffReader = new BufferedReader(new FileReader(fileName));
		String line;
		int priceMax = -1;
		
		while((line = buffReader.readLine()) != null){
			int price = getNthTokenFromLine(line, 4);
			if(price > priceMax){
				priceMax = price;
				carMax = line;
			}
		}
		Car mostExpensive = getInstance(carMax);
		buffReader.close();
		return mostExpensive;
		
	}
	
	private static int getNthTokenFromLine(String line, int n){
		StringTokenizer st = new StringTokenizer(line);
		int price = 0;
		for(int i = 1; i <= 4; i++){
			if(i < n){
				st.nextToken();
			} else {
				price = Integer.parseInt(st.nextToken());
			}
		}
		return price;
	}
	
	private static Car getInstance(String carString){
		StringTokenizer st = new StringTokenizer(carString);
		String model = st.nextToken();
		int annoManifattura = Integer.parseInt(st.nextToken());
		double km = Double.parseDouble(st.nextToken());
		int price = Integer.parseInt(st.nextToken());
		
		return new Car(model, annoManifattura, km, price);
		
	}
	
}

