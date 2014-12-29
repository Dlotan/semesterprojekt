package main;

import java.util.Map;

public class GeneratorJSONParser {
	private final String name;
	private final Map<String, Double> args;
	public GeneratorJSONParser(String name, Map<String, Double> args) {
		this.name = name;
		this.args = args;
	}
	public String getName() {
		return name;
	}
	public Map<String, Double> getArguments() {
		return args;
	}
}
