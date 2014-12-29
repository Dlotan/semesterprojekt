package random;

import java.util.Map;

public class GeneratorLognormal implements RandomGenerator {
	
	private double a;
	private GeneratorNormal generatorNormal;
	
	public GeneratorLognormal(Map<String, Double> arguments) {
		a = arguments.get("a");
		generatorNormal = new GeneratorNormal(arguments);
	}
	
	@Override
	public double getNumber() {
		return a + Math.exp(generatorNormal.getNumber());
	}

	@Override
	public double getTest(double x) {
		double logarithmic = Math.log(x);
		return generatorNormal.getTest(logarithmic);
		
	}

}
