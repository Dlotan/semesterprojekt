package main;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import random.GeneratorCauchy;
import random.GeneratorCosine;
import random.GeneratorErlang;
import random.GeneratorLognormal;
import random.GeneratorNormal;
import random.GeneratorWeibull;
import random.RandomGenerator;

public class MainTest {
	final int randomNumbers = 5000;
	final int initialClasses = 400;
	RandomGenerator generator;
	DiceMaster diceMaster;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testCosine() {
		System.out.println("Cosine");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("xMin", 0.0);
		arguments.put("xMax", 1.0);
		generator = new GeneratorCosine(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
				else {
					System.out.println("Not enough final classes");
				}
			}
			else {
				System.out.println("Chi square not true");
			}
		}
		assertTrue("No win", win);
	}

	@Test
	public void testCauchy() {
		System.out.println("Cauchy");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("a", 0.0);
		arguments.put("b", 0.5);
		generator = new GeneratorCauchy(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
			}
		}
		assertTrue("No win", win);
	}

	@Test
	public void testErlang() {
		System.out.println("Erlang");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("b", 1.);
		arguments.put("c", 2.);
		generator = new GeneratorErlang(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
			}
		}
		assertTrue("No win", win);
	}

	@Test
	public void testWeibull() {
		System.out.println("Weibull");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("a", 0.0);
		arguments.put("b", 1.);
		arguments.put("c", 2.);
		generator = new GeneratorWeibull(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
			}
		}
		assertTrue("No win", win);
	}
	@Test
	public void testNormal() {
		System.out.println("Normal");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("mu", 0.0);
		arguments.put("sigma", 1.);
		generator = new GeneratorNormal(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
			}
		}
		assertTrue("No win", win);
	}
	
	@Test
	public void testLogormal() {
		System.out.println("Lognormal");
		Map<String, Double> arguments = new HashMap<>();
		arguments.put("a", 0.);
		arguments.put("mu", 0.0);
		arguments.put("sigma", 1.);
		generator = new GeneratorLognormal(arguments);
		diceMaster = new DiceMaster(generator, initialClasses, randomNumbers);
		boolean win = false;
		for (int i = 0; i < 300; i++) {
			List<Double> numbers = diceMaster.getRandomNumbers();
			assertTrue("Wrong size of numbers", numbers.size() == randomNumbers);
			if (diceMaster.checkChiSquare(numbers) == true) {
				if (diceMaster.getRandomRangeClasses(numbers).size() > 100) {
					win = true;
					break;
				}
			}
		}
		assertTrue("No win", win);
	}

}
