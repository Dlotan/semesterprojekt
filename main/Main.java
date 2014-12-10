package main;

import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import random.GeneratorCauchy;
import random.GeneratorCosine;
import random.GeneratorErlang;
import random.GeneratorWeibull;
import random.RandomGenerator;

public class Main {
	private static String databaseName = "D";
	public static void main(String[] args) {
		int randomNumbers = 0;
		int initialClasses = 0;
		String generatorName;
		Map<String, Double> arguments = new HashMap<String, Double>();
		try {
			JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(args[0]));
 
            JSONObject jsonObject = (JSONObject) obj;
            generatorName = (String) jsonObject.get("generator");
            randomNumbers = Integer.parseInt((String) jsonObject.get("randomNumbers"));
            initialClasses = Integer.parseInt((String) jsonObject.get("initialClasses"));
            JSONArray argumentsList = (JSONArray) jsonObject.get("arguments");
 
            for (int i = 0; i < argumentsList.size(); i++)
            {
            	JSONObject argument = (JSONObject) argumentsList.get(i);
            	String key = (String) argument.get("key");
            	double value = Double.parseDouble((String) argument.get("value"));
            	arguments.put(key, value);
            }
 
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		System.out.println("Generator name ist " + generatorName);
		System.out.println("random numbers " + randomNumbers);
		System.out.println("initial classes " + initialClasses);
		RandomGenerator generator = null;
		switch(generatorName){
		case "cosine": generator = new GeneratorCosine(arguments);
			break;
		case "weibull": generator = new GeneratorWeibull(arguments);
			break;
		case "cauchy": generator = new GeneratorCauchy(arguments);
			break;
		case "erlang": generator = new GeneratorErlang(arguments);
			break;
		}
		final DiceMaster diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		System.out.println("---WÜRFELN---");
		List<Double> numbers;
		do{
			numbers = diceMaster.getRandomNumbers();
		}while(diceMaster.checkChiSquare(numbers) == false); 
		System.out.println("---END-WÜRFELN---");
		double min = Collections.min(numbers);
		double max = Collections.max(numbers);
		System.out.println("Min ist " + min);
		System.out.println("Max ist " + max);
		Database database = new Database(databaseName);
		database.clear();
		database.fill(numbers);
		database.createIndex();
		//database.output();
		database.close();
	}
}
