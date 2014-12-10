package random;

import java.util.Map;
import java.util.Random;

public class GeneratorCosine implements RandomGenerator{
	double xMin;
	double xMax;
	public GeneratorCosine(Map<String, Double> arguments) {
		xMin = arguments.get("xMin");
		xMax = arguments.get("xMax");
	}
	@Override
	public double getNumber() {
		assert(xMin < xMax);
        double a = 0.5 * (xMin + xMax);
        double b = (xMax - xMin) / Math.PI;
        double random_number = (new Random().nextDouble() * 2) - 1; // -1 to 1
        return a + b * Math.asin(random_number);
	}

	@Override
	public double getTest(double x) {
		double a = (xMin + xMax) / 2;
		double b = (xMax - xMin) / Math.PI;
		return (0.5 * (1 + Math.sin((x -a) / b)));
	}

}
