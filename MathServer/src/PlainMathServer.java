/* una implementazione del servizio MathService*/

public class PlainMathServer implements MathService{

	@Override
	public double add(double a, double b) {
		return a+b;
	}

	@Override
	public double sub(double a, double b) {
		return a-b;
	}

	@Override
	public double div(double a, double b) {
		if(b != 0) return a/b;
		else return Double.MAX_VALUE;
	}

	@Override
	public double mul(double a, double b) {
		return a*b;
	}
	
}