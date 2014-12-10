package main;

import java.util.Collections;
import java.util.List;

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
		List<Integer> intNumbers = NumberConverter.convertDoubleList(numbers);
		System.out.println("---END-WÜRFELN---");
		double min = Collections.min(numbers);
		double max = Collections.max(numbers);
		System.out.println("Min ist " + min);
		System.out.println("Max ist " + max);
		Database database = new Database(databaseName);
		database.clear();
		database.fill(intNumbers);
		database.createIndex();
		database.output();
		database.close();
	}
}
