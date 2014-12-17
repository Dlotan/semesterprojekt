package main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CustomJSONParser {
	private int numRandomNumbers = 0;
	private int numInitialClasses = 0;
	private String name;
	List<GeneratorJSONParser> generators;
	private GeneratorJSONParser getArgumentsFromJSONObject(JSONObject generator) throws Exception{
		Map<String, Double> arguments = new HashMap<>();
		String name = (String) generator.get("name");
		boolean create =  (Boolean) generator.get("create");
		boolean profile = (Boolean) generator.get("profile");
		JSONArray argumentsList = (JSONArray) generator.get("arguments");
		for (int j = 0; j < argumentsList.size(); j++) {
			JSONObject argument = (JSONObject) argumentsList.get(j);
			String key = (String) argument.get("key");
			double value = Double.parseDouble((String) argument
					.get("value"));
			arguments.put(key, value);
		}
		return new GeneratorJSONParser(name, create, profile, arguments);
	}
	public CustomJSONParser(String path) {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) obj;
			name = (String) jsonObject.get("name");
			numRandomNumbers = Integer.parseInt((String) jsonObject
					.get("randomNumbers"));
			numInitialClasses = Integer.parseInt((String) jsonObject
					.get("initialClasses"));
			generators = new ArrayList<>();
			JSONArray generatorsJSON = (JSONArray) jsonObject.get("generators");
			for (int j = 0; j < generatorsJSON.size(); j++) {
				JSONObject generator = (JSONObject) generatorsJSON.get(j);
				generators.add(getArgumentsFromJSONObject(generator));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	public int getNumRandomNumbers() {
		return numRandomNumbers;
	}
	public int getNumInitialClasses() {
		return numInitialClasses;
	}
	public String getName() {
		return name;
	}
	public List<GeneratorJSONParser> getGenerators() {
		return generators;
	}
}
