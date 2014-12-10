package main;

import java.util.List;
import java.util.Vector;

public class RandomRangeClass {
	private double min;
	private double max;
	private List<Double> values;

	public boolean isDeleted = false;

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public List<Double> getValues() {
		return values;
	}

	public void clearValues() {
		values.clear();
	}

	public void setDeleted() {
		isDeleted = true;
	}

	public RandomRangeClass(double min, double max) {
		this.min = min;
		this.max = max;
		values = new Vector<Double>();
	}

	void addValue(double value) {
		values.add(value);
	}

	void addRandomRange(RandomRangeClass other) {
		values.addAll(other.getValues());
		this.min = this.min < other.getMin() ? this.min : other.getMin();
		this.max = this.max > other.getMax() ? this.max : other.getMax();
		other.setDeleted();
		other.clearValues();
	}

	public String toString() {
		return "(" + values.toString() + " " + Boolean.toString(isDeleted)
				+ ")";
	}
}
