package main;

import java.util.Map;

public class GeneratorJSONParser {
	private final String name;
	private final Map<String, Double> args;
	private final boolean create;
	private final boolean profile;
	public GeneratorJSONParser(String name, boolean create, boolean profile,Map<String, Double> args) {
		this.name = name;
		this.create = create;
		this.profile = profile;
		this.args = args;
	}
	public String getName() {
		return name;
	}
	public Map<String, Double> getArguments() {
		return args;
	}
	public boolean isProfile() {
		return profile;
	}
	public boolean isCreate() {
		return create;
	}
}
