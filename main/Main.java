package main;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import dbqueries.QueryGenerator;
import dbqueries.QueryProfiler;
import dbqueries.RangeQuery;
//import dbqueries.RangeQuery;
import dbqueries.SingleQuery;
import random.GeneratorCauchy;
import random.GeneratorCosine;
import random.GeneratorErlang;
import random.GeneratorWeibull;
import random.RandomGenerator;

public class Main {
	
	public static double getMean(List<Double> numbers) {
		double sum = 0;
		for(double number : numbers) {
			sum += number;
		}
		return sum / numbers.size();
	}

	public static void main(String[] args) {
		try {
			PrintWriter writer = new PrintWriter("output.csv", "UTF-8");
			CustomJSONParser parser = new CustomJSONParser(args[0]);
			System.out.println("Test Case Name: " + parser.getName());
			System.out.println("random numbers: " + parser.getNumRandomNumbers());
			System.out.println("initial classes: " + parser.getNumInitialClasses());
			System.out.println("warmup iterations: " + parser.getWarmupIterations());
			System.out.println("mean iterations: " + parser.getMeanIterations());
			System.out.println("cycles per generator: " + parser.getCyclesPerGenerator());
			QueryProfiler.warmupIterations = parser.getWarmupIterations();
			QueryProfiler.meanIterations = parser.getMeanIterations();
			writer.println("Name;Random Numbers;Initial Classes;Warmup Iterations;Mean Iterations;Cycles per Generator");
			writer.println(parser.getName() + ";"
				+ parser.getNumRandomNumbers() + ";" 
				+ parser.getNumInitialClasses() + ";"
				+ parser.getWarmupIterations() + ";"
				+ parser.getMeanIterations() + ";"
				+ parser.getCyclesPerGenerator() + ";");
			for(GeneratorJSONParser generatorJSON : parser.getGenerators()) {
				Database database = new Database(Database.databaseName, generatorJSON.getName());
				System.out.println("Generator name: " + generatorJSON.getName());
				System.out.println("Arguments: " + generatorJSON.getArguments().toString());
				if(parser.isCreate()) {
					RandomGenerator generator = null;
					switch (generatorJSON.getName()) {
					case "cosine":
						generator = new GeneratorCosine(generatorJSON.getArguments());
						break;
					case "weibull":
						generator = new GeneratorWeibull(generatorJSON.getArguments());
						break;
					case "cauchy":
						generator = new GeneratorCauchy(generatorJSON.getArguments());
						break;
					case "erlang":
						generator = new GeneratorErlang(generatorJSON.getArguments());
						break;
					}
					final DiceMaster diceMaster = new DiceMaster(generator, parser.getNumInitialClasses(),
							parser.getNumRandomNumbers());
					System.out.println("---WÜRFELN---");
					List<Double> numbers;
					while(true) {
						numbers = diceMaster.getRandomNumbers();
						if(diceMaster.checkChiSquare(numbers)) {
							break;
						}
						else {
							System.out.println("Chi square nicht getroffen");
						}
					}
					System.out.println("---END-WÜRFELN---");
					double min = Collections.min(numbers);
					double max = Collections.max(numbers);
					System.out.println("Min ist " + min);
					System.out.println("Max ist " + max);
					List<Integer> intNumbers = NumberConverter.convertDoubleList(numbers);
					database.connect();
					database.clear();
					database.fill(intNumbers);
					database.createIndex();
					database.disconnect();
				}
				if(parser.isProfile()) {
					System.out.println("Read Database");
					database.connect();
					QueryGenerator queryGenerator = new QueryGenerator(database, database.getNumbers());
					for(int j = 0; j < parser.getCyclesPerGenerator(); j++)
					{
						System.out.println("Profile");
						QueryProfiler queryProfiler = new QueryProfiler(database);
						if(parser.isSingleQueries()) {
							List<SingleQuery> singleQueries = queryGenerator.getSingleQueries();
							List<Double> results = queryProfiler.profileSingleQueries(singleQueries);
							writer.print(generatorJSON.getName() + ";SingleQueries;");
							for(int i = 0; i < results.size(); i++) {
								writer.print(new Double(results.get(i)).toString().replace(".", ",") + ";");
							}
							String mean = new Double(getMean(results)).toString().replace(".", ",");
							System.out.println("Mean is: " + mean);
							writer.print(";" + mean + ";");
							writer.println();
						}
						if(parser.isRangeQueries()) {
							List<RangeQuery> rangeQueries = queryGenerator.getRangeQueries();
							List<Double> results = queryProfiler.profileRangeQueries(rangeQueries);
							writer.print(generatorJSON.getName() + ";RangeQueries;");
							for(int i = 0; i < results.size(); i++) {
								writer.print(new Double(results.get(i)).toString().replace(".", ",") + ";");
							}
							String mean = new Double(getMean(results)).toString().replace(".", ",");
							System.out.println("Mean is: " + mean);
							writer.print(";" + mean + ";");
							writer.println();
						}
					}
				}
			}
			writer.close();
			System.out.println("END");
		} catch(Exception e) {
			e.printStackTrace();
		}
 	}
}
