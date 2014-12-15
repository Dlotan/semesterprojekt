package main;

import java.util.Collections;
import java.util.List;

import dbqueries.QueryGenerator;
import dbqueries.QueryProfiler;
import dbqueries.RangeQuery;
import random.GeneratorCauchy;
import random.GeneratorCosine;
import random.GeneratorErlang;
import random.GeneratorWeibull;
import random.RandomGenerator;

public class Main {
	private static String databaseName = "D";

	public static void main(String[] args) {
		JSONRandomGeneratorParser parser = new JSONRandomGeneratorParser(args[0]);
		System.out.println("Generator name ist " + parser.getGeneratorName());
		System.out.println("random numbers " + parser.getNumRandomNumbers());
		System.out.println("initial classes " + parser.getNumInitialClasses());
		RandomGenerator generator = null;
		switch (parser.getGeneratorName()) {
		case "cosine":
			generator = new GeneratorCosine(parser.getArguments());
			break;
		case "weibull":
			generator = new GeneratorWeibull(parser.getArguments());
			break;
		case "cauchy":
			generator = new GeneratorCauchy(parser.getArguments());
			break;
		case "erlang":
			generator = new GeneratorErlang(parser.getArguments());
			break;
		}
		final DiceMaster diceMaster = new DiceMaster(generator, parser.getNumInitialClasses(),
				parser.getNumRandomNumbers());
		System.out.println("---WÜRFELN---");
		List<Double> numbers;
		do {
			numbers = diceMaster.getRandomNumbers();
		} while (diceMaster.checkChiSquare(numbers) == false);
		System.out.println("---END-WÜRFELN---");
		double min = Collections.min(numbers);
		double max = Collections.max(numbers);
		System.out.println("Min ist " + min);
		System.out.println("Max ist " + max);
		List<Integer> intNumbers = NumberConverter.convertDoubleList(numbers);
		QueryGenerator queryGenerator = new QueryGenerator(intNumbers);
		Database database = new Database(databaseName);
		database.clear();
		database.fill(intNumbers);
		database.createIndex();
		//List<SingleQuery> singleQueries = queryGenerator.getSingleQueries();
		List<RangeQuery> rangeQueries = queryGenerator.getRangeQueries();
		QueryProfiler queryProfiler = new QueryProfiler(database.conn);
		/*List<Long> results = queryProfiler.profileSingleQueries(singleQueries);
		for(int i = 0; i < results.size(); i++) {
			System.out.println(singleQueries.get(i).getQueryString());
			System.out.println(results.get(i));
		}*/
		List<Long> results = queryProfiler.profileRangeQueries(rangeQueries);
		for(int i = 0; i < results.size(); i++) {
			System.out.println(rangeQueries.get(i).getQueryString());
			System.out.println(results.get(i));
		}
		//database.output();
		database.close();
	}
}
