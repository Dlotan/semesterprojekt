package main;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class NumberConverter {
	// Translates a range to range 0 to 1 Billion (Milliarde)
	// Example range: -1 to 1 -> 0 to 1 Billion
	static List<Integer> convertDoubleList(List<Double> source) {
		double min = Collections.min(source);
		double max = Collections.max(source);
		// 1) Make min 0 -> min = 5 -> 10 will be 5 
		// example: [1,2,5,10] -> [0,1,4,9]
		// 2) Rule of three to make biggest to be 1 billion.
		// example: [0,1,2,3,4,5] -> [0,200000000,400000000,600000000,800000000,1000000000]
		max = max - min;
		double multiplier = 1000000000 / max;
		for(int i = 0; i < source.size(); i++) {
			source.set(i, (source.get(i) - min) * multiplier);
		}
		// Database uses Integer.
		Vector<Integer> result = new Vector<>();
		result.ensureCapacity(source.size());
		for(double number : source) {
			result.add(new Double(number).intValue());
		}
		
		return result;
	}
}
