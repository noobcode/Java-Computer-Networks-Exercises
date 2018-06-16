import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MathServer {
	
	protected MathService mathService;
	protected Socket socket;
		
	public static void main(String[] args) throws IOException {
		int port = 10000;
		
		if(args.length == 1){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){ }
		}
		System.out.println("math server is running...");
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true){
			Socket socket = serverSocket.accept();
			MathServer mathServer = new MathServer();
			mathServer.setMathService(new PlainMathServer());
			mathServer.setSocket(socket);
			mathServer.execute();
			
		}
		

	}
	
	public void setMathService(PlainMathServer plainMathServer){
		mathService = plainMathServer;
	}
	
	public void setSocket(Socket s){
		socket = s;
	}
	
	public void  execute(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = reader.readLine();
			double result = parseExecution(line);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(""+result);
			writer.newLine();
			writer.flush();
			reader.close();
			writer.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public double parseExecution(String line){
		double result = Double.MAX_VALUE;
		String[] elements = line.split(":");
		if(elements.length != 3)
			throw new IllegalArgumentException("parsing error!");
		double a = 0, b = 0;
		
		try{
			a = Double.parseDouble(elements[1]);
			b = Double.parseDouble(elements[2]);
		} catch(Exception e){
			throw new IllegalArgumentException("invalid arguments!");
		}
		
		switch(elements[0].charAt(0)){
			case '+':	result = mathService.add(a, b); break;
			case '-':	result = mathService.sub(a, b); break;
			case '*':	result = mathService.mul(a, b); break;
			case '/':	result = mathService.div(a, b); break;
			default: throw new IllegalArgumentException("invalid math operation!");
		}
		return result;
	}
}
