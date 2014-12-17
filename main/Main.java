package main;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import dbqueries.QueryGenerator;
import dbqueries.QueryProfiler;
import dbqueries.RangeQuery;
import dbqueries.SingleQuery;
import random.GeneratorCauchy;
import random.GeneratorCosine;
import random.GeneratorErlang;
import random.GeneratorWeibull;
import random.RandomGenerator;

public class Main {
	
	public static double getMean(List<Long> numbers) {
		long sum = 0;
		for(long number : numbers) {
			sum += number;
		}
		return sum / (numbers.size() * 1.0);
	}

	public static void main(String[] args) {
		try {
			PrintWriter writer = new PrintWriter("output.csv", "UTF-8");
			CustomJSONParser parser = new CustomJSONParser(args[0]);
			System.out.println("Test Case Name: " + parser.getName());
			System.out.println("random numbers " + parser.getNumRandomNumbers());
			System.out.println("initial classes " + parser.getNumInitialClasses());
			writer.println("Name;Random Numbers;Initial Classes;");
			writer.println(parser.getName() + ";" + parser.getNumRandomNumbers() + ";" 
				+ parser.getNumInitialClasses() + ";");
			for(GeneratorJSONParser generatorJSON : parser.getGenerators()) {
				Database.tableName = "T" + generatorJSON.getName();
				Database.indexName = "I" + generatorJSON.getName();
				System.out.println("Generator name: " + generatorJSON.getName());
				System.out.println("Arguments: " + generatorJSON.getArguments().toString());
				if(generatorJSON.isCreate()) {
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
					Database database = new Database(Database.databaseName);
					database.clear();
					database.fill(intNumbers);
					database.createIndex();
					database.close();
				}
				if(generatorJSON.isProfile()) {
					for(int j = 0; j < 3; j++)
					{
						System.out.println("Profile");
						Database database = new Database(Database.databaseName);
						QueryGenerator queryGenerator = new QueryGenerator(database.getNumbers());
						database.close();
						List<SingleQuery> singleQueries = queryGenerator.getSingleQueries();
						List<RangeQuery> rangeQueries = queryGenerator.getRangeQueries();
						QueryProfiler queryProfiler = new QueryProfiler();
						List<Long> results = queryProfiler.profileSingleQueries(singleQueries);
						writer.print(generatorJSON.getName() + ";SingleQueries;");
						for(int i = 0; i < results.size(); i++) {
							writer.print(results.get(i) + ";");
						}
						String mean = new Double(getMean(results)).toString().replace(".", ",");
						System.out.println("Mean is: " + mean);
						String meanDivided = new Double(getMean(results) / 1000000.).toString().replace(".", ",");
						writer.print(";" + mean + ";;" + meanDivided + ";");
						writer.println();
						/*results = queryProfiler.profileRangeQueries(rangeQueries);
						writer.print(generatorJSON.getName() + ";RangeQueries;");
						for(int i = 0; i < results.size(); i++) {
							writer.print(results.get(i) + ";");
						}
						writer.println();*/
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
