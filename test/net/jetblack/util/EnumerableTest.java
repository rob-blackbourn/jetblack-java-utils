package net.jetblack.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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

	@Test
	public void createReverseArray() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.createReverse(sourceArray);
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceArray.length == list.size());
		for (int i = 0, j = sourceArray.length - 1; i < sourceArray.length; ++i, --j) {
			assertTrue(sourceArray[i] == list.get(j));
		}
	}
	
	@Test
	public void createList() {
		List<Integer> sourceList = Arrays.asList(1, 2, 3, 4, 5);
		Enumerable<Integer> enumerable = Enumerable.create(sourceList);
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceList.size() == list.size());
		for (int i = 0; i < sourceList.size(); ++i) {
			assertTrue(sourceList.get(i) == list.get(i));
		}
	}

	@Test
	public void createReverseList() {
		List<Integer> sourceList = Arrays.asList(1, 2, 3, 4, 5);
		Enumerable<Integer> enumerable = Enumerable.createReverse(sourceList.listIterator());
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceList.size() == list.size());
		for (int i = 0, j = sourceList.size() - 1; i < sourceList.size(); ++i, --j) {
			assertTrue(sourceList.get(i) == list.get(j));
		}
	}
}
