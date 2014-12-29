package random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratorNormal implements RandomGenerator {
	private double mu, sigma;
	private List<List<Double>> vergleichsWerte;
	
	public GeneratorNormal(Map<String, Double> arguments) {
		mu = arguments.get("mu");
		sigma = arguments.get("sigma");
		vergleichsWerte = new ArrayList<List<Double>>();
		
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/test.csv"));
		    try {
		        while((line = br.readLine()) != null && line.length() != 0) {
		        	List<Double> reihe = new ArrayList<>();
		        	String[] spalten = line.split(";");
		        	for(String spalte : spalten) {
		        		reihe.add(Double.parseDouble(spalte));
		        	}
		        	vergleichsWerte.add(reihe);
		        }
		    } finally {
		        br.close();
		    }
		}catch(Exception e) {
			System.out.println("EXCEPTION");
		}
	}
	@Override
	public double getNumber() {
		assert(sigma > 0);
		double p,p1,p2;
		do {
			p1 = (new Random().nextDouble() * 2) - 1; // -1 to 1
			p2 = (new Random().nextDouble() * 2) - 1; // -1 to 1
			p = Math.pow(p1, 2) + Math.pow(p2, 2);
		} while(p >= 1.);
		return mu + sigma * p1 * Math.sqrt(-2. * Math.log(p) / p);
	}

	@Override
	public double getTest(double x) {
		double logarithmic;
		
		logarithmic = x; // Hier stand früher das mit Log => brauchen wir nicht .. müssen ja direkt nach x in der tabelle suchen
		boolean negative = logarithmic < 0;
		if(logarithmic < -4.09) {
			return 0;
		}
		if(logarithmic > 4.09) {
			return 0;
		} 
		
		if(negative) {
			logarithmic = logarithmic * -1;
		}
		int temp = (int) (logarithmic * 100);
		int reihe = temp / 10;
		int spalte = temp % 10;
		double ergebniss = vergleichsWerte.get(reihe).get(spalte);
		if(negative) {
			return 1 - ergebniss;
		}
		else {
			return ergebniss;
		}
	}

}
