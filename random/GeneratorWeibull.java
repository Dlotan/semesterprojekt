package random;

import java.util.Map;
import java.util.Random;

public class GeneratorWeibull implements RandomGenerator {
	double a, b, c;

	public GeneratorWeibull(Map<String, Double> arguments) {
		a = arguments.get("a");
		b = arguments.get("b");
		c = arguments.get("b");
	}

	@Override
	public double getNumber() {
		assert (b > 0. && c > 0.);
		double randomNumber = new Random().nextDouble();
		return a + b * Math.pow(-Math.log(randomNumber), 1. / c);
	}

	@Override
	public double getTest(double x) {
		return 1 - Math.pow(Math.E, (-1 * Math.pow(((x - a) / b), c)));
	}

}
