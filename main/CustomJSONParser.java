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
	private String name;
	private int numRandomNumbers = 0;
	private int numInitialClasses = 0;
	private int warmupIterations = 0;
	private int meanIterations = 0;
	private int cyclesPerGenerator = 0;
	private boolean singleQueries = false;
	private boolean rangeQueries = false;
	private boolean create = false;
	private boolean profile = false;
	List<GeneratorJSONParser> generators;
	private GeneratorJSONParser getArgumentsFromJSONObject(JSONObject generator) throws Exception{
		Map<String, Double> arguments = new HashMap<>();
		String name = (String) generator.get("name");
		JSONArray argumentsList = (JSONArray) generator.get("arguments");
		for (int j = 0; j < argumentsList.size(); j++) {
			JSONObject argument = (JSONObject) argumentsList.get(j);
			String key = (String) argument.get("key");
			double value = Double.parseDouble((String) argument
					.get("value"));
			arguments.put(key, value);
		}
		return new GeneratorJSONParser(name, arguments);
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
			warmupIterations = Integer.parseInt((String) jsonObject
					.get("warmupIterations"));
			meanIterations = Integer.parseInt((String) jsonObject
					.get("meanIterations"));
			cyclesPerGenerator = Integer.parseInt((String) jsonObject
					.get("cyclesPerGenerator"));
			singleQueries =  (Boolean) jsonObject.get("singleQueries");
			rangeQueries = (Boolean) jsonObject.get("rangeQueries");
			create =  (Boolean) jsonObject.get("create");
			profile = (Boolean) jsonObject.get("profile");
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
	public int getWarmupIterations() {
		return warmupIterations;
	}
	public int getMeanIterations() {
		return meanIterations;
	}
	public int getCyclesPerGenerator() {
		return cyclesPerGenerator;
	}
	public boolean isSingleQueries() {
		return singleQueries;
	}
	public boolean isRangeQueries() {
		return rangeQueries;
	}
	public boolean isProfile() {
		return profile;
	}
	public boolean isCreate() {
		return create;
	}
}
