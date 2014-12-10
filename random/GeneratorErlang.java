package random;

import java.util.Map;
import java.util.Random;

public class GeneratorErlang implements RandomGenerator {
	double b, c;

	public GeneratorErlang(Map<String, Double> arguments) {
		b = arguments.get("b");
		c = arguments.get("c");
	}

	@Override
	public double getNumber() {
		assert (b > 0. && c >= 1);
		double prod = 1.0;
		for (int i = 0; i < c; i++) {
			double random_number = new Random().nextDouble();
			prod *= random_number;
		}
		return -b * Math.log(prod);
	}

	public static int factorial(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	@Override
	public double getTest(double x) {
		double ersterTeil = Math.pow(Math.E, (-1 * x) / b);
		double zweiterTeil = 0;
		for (int i = 0; i < c; i++) {
			zweiterTeil += Math.pow(x / b, i) / factorial(i);
		}
		return 1 - (ersterTeil * zweiterTeil);
	}

}
