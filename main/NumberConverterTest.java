package main;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class NumberConverterTest {

	@Test
	public void testInteger() {
		List<Double> source = new Vector<>();
		for(int i = -10; i <= 10; i++) {
			source.add(i * 1.0);
		}
		List<Integer> result = NumberConverter.convertDoubleList(source);
		System.out.println(result);
		assertTrue("Wrong size", result.size() == 21);
		assertTrue("Wrong min", Collections.min(result) == 0);
		assertTrue("Wrong max", Collections.max(result) == 1000000000);
		assertTrue(result.contains(500000000));
	}
	
	@Test
	public void testFloat() {
		List<Double> source = new Vector<>();
		for(float i = -10; i <= 10; i+= 0.5) {
			source.add(i * 1.0);
		}
		List<Integer> result = NumberConverter.convertDoubleList(source);
		System.out.println(result);
		assertTrue("Wrong min", Collections.min(result) == 0);
		assertTrue("Wrong max", Collections.max(result) == 1000000000);
	}

}
