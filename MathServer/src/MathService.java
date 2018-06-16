/* definizione di un generico servizio di calcolo matematico. alla seguente
 * interfaccia possono corrispondere diverse implementazioni. */

public interface MathService {
	public double add(double a, double b);
	public double sub(double a, double b);
	public double div(double a, double b);
	public double mul(double a, double b);
}
