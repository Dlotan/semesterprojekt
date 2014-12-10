package main;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import random.RandomGenerator;

public class DiceMaster {
	private int num_randomRangeClasses;
	private int num_randomNumbers;
	private RandomGenerator generator;
	public DiceMaster(RandomGenerator generator, int num_randomRangeClasses, int num_randomNumbers) {
		this.num_randomRangeClasses = num_randomRangeClasses;
		this.num_randomNumbers = num_randomNumbers;
		this.generator = generator;
	}
	List<Double> getRandomNumbers() {
		Vector<Double> result = new Vector<Double>();
		result.ensureCapacity(num_randomNumbers);
		for(int i = 0; i < num_randomNumbers; i++) {
			result.add(generator.getNumber());
		}
		return result;
	}
	List<RandomRangeClass> getRandomRangeClasses(List<Double> numbers) {
		double max = Collections.max(numbers);
		double min = Collections.min(numbers);
		double range = (max - min) / num_randomRangeClasses;
		double currentMin = min;
		Vector<Double> copyNumbers = new Vector<Double>();
		copyNumbers.ensureCapacity(num_randomNumbers);
		for(Double number : numbers) {
			copyNumbers.add(number);
		}
		Collections.sort(copyNumbers);
		Vector<RandomRangeClass> randomRangeClasses = new Vector<RandomRangeClass>();
		randomRangeClasses.ensureCapacity(num_randomRangeClasses);
		int currentIndex = 0;
		while(currentMin < max) {
			double currentMax = currentMin + range;
			RandomRangeClass temp = new RandomRangeClass(currentMin, currentMax);
			while(currentIndex < num_randomNumbers && 
					copyNumbers.elementAt(currentIndex) < currentMax) {
				temp.addValue(copyNumbers.elementAt(currentIndex));
				currentIndex++;
			}
			randomRangeClasses.add(temp);
			currentMin += range;
		}
		for(int i = 0; i < randomRangeClasses.size(); i++) {
			if(randomRangeClasses.elementAt(i).getValues().size() < 5) {
				int j = i + 1;
				for(; j < randomRangeClasses.size(); j++) {
					if(randomRangeClasses.elementAt(j).getValues().size() == 0) {
						randomRangeClasses.elementAt(j).setDeleted();
					}
					else {
						randomRangeClasses.elementAt(j).addRandomRange(randomRangeClasses.elementAt(i));
						break;
					}
				}
				i = j - 1; // j wird direkt inkrementiert desswegen -1
			}
		}
		Vector<RandomRangeClass> result = new Vector<RandomRangeClass>();
		for(RandomRangeClass randomRangeClass : randomRangeClasses) {
			if(randomRangeClass.isDeleted == false) {
				result.add(randomRangeClass);
			}
		}
		return result;
	}
	boolean checkChiSquare(List<Double> numbers) {
		List<RandomRangeClass> randomRangeClasses = getRandomRangeClasses(numbers);
		if(randomRangeClasses.size() <= 100) {
			System.out.println("Not enough Classes");
			return false;
		}
		double chi_square = 0;
		for(RandomRangeClass randomRangeClass : randomRangeClasses) {
			double erwartet = generator.getTest(randomRangeClass.getMax()) - generator.getTest(randomRangeClass.getMin());
			erwartet *= num_randomNumbers;
			chi_square += Math.pow(randomRangeClass.getValues().size() - erwartet, 2) / erwartet;
		}
		int j = randomRangeClasses.size();
		System.out.println("RangeClasses " + j);
		double z = 1.282;
		double checksumme = Math.sqrt(2 * (j - 1));
		checksumme = Math.pow(checksumme + z, 2);
		checksumme *= 0.5;
		System.out.println("Chcecksumme " + checksumme);
		System.out.println("Chi square " + chi_square);
		return checksumme > chi_square;
	}
}
