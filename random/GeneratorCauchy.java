package random;

import java.util.Map;
import java.util.Random;

public class GeneratorCauchy implements RandomGenerator {

	double a;
	double b;

	public GeneratorCauchy(Map<String, Double> arguments) {
		a = arguments.get("a");
		b = arguments.get("b");
	}

	@Override
	public double getNumber() {
		double random_number = (new Random().nextDouble()) - 1; // -0.5 to 0.5
		assert (b > 0.);
		return a + b * Math.tan(Math.PI * random_number);
	}

	@Override
	public double getTest(double x) {
		return 0.5 + ((1 / Math.PI) * (Math.atan((x - a) / b)));
	}

}
