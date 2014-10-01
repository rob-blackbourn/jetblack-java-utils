package net.jetblack.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class EnumerableTest {

	@Test
	public void testCreateArray() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray);
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceArray.length == list.size());
		for (int i = 0; i < sourceArray.length; ++i) {
			assertTrue(sourceArray[i] == list.get(i));
		}
	}

}
