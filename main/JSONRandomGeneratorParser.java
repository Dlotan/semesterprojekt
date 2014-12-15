package main;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONRandomGeneratorParser {
	private int numRandomNumbers = 0;
	private int numInitialClasses = 0;
	private String generatorName;
	private Map<String, Double> arguments;
	public JSONRandomGeneratorParser(String path) {
		arguments = new HashMap<String, Double>();
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));

			JSONObject jsonObject = (JSONObject) obj;
			generatorName = (String) jsonObject.get("generator");
			numRandomNumbers = Integer.parseInt((String) jsonObject
					.get("randomNumbers"));
			numInitialClasses = Integer.parseInt((String) jsonObject
					.get("initialClasses"));
			JSONArray argumentsList = (JSONArray) jsonObject.get("arguments");

			for (int i = 0; i < argumentsList.size(); i++) {
				JSONObject argument = (JSONObject) argumentsList.get(i);
				String key = (String) argument.get("key");
				double value = Double.parseDouble((String) argument
						.get("value"));
				arguments.put(key, value);
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
	public String getGeneratorName() {
		return generatorName;
	}
	public Map<String, Double> getArguments() {
		return arguments;
	}
}
