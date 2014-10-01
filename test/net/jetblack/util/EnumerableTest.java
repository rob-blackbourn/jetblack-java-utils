package net.jetblack.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jetblack.util.invokables.UnaryFunction;
import net.jetblack.util.selectors.IdentitySelector;
import net.jetblack.util.selectors.ToStringSelector;

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
			assertEquals(sourceArray[i], list.get(i));
		}
	}

	@Test
	public void testCreateReverseArray() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.createReverse(sourceArray);
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceArray.length == list.size());
		for (int i = 0, j = sourceArray.length - 1; i < sourceArray.length; ++i, --j) {
			assertEquals(sourceArray[i], list.get(j));
		}
	}
	
	@Test
	public void testCreateIterator() {
		List<Integer> sourceList = Arrays.asList(1, 2, 3, 4, 5);
		Enumerable<Integer> enumerable = Enumerable.create(sourceList);
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceList.size() == list.size());
		for (int i = 0; i < sourceList.size(); ++i) {
			assertEquals(sourceList.get(i), list.get(i));
		}
	}

	@Test
	public void testCreateReverseListIterator() {
		List<Integer> sourceList = Arrays.asList(1, 2, 3, 4, 5);
		Enumerable<Integer> enumerable = Enumerable.createReverse(sourceList.listIterator());
		List<Integer> list = new ArrayList<Integer>();
		for (Integer value : enumerable) {
			list.add(value);
		}
		assertTrue(sourceList.size() == list.size());
		for (int i = 0, j = sourceList.size() - 1; i < sourceList.size(); ++i, --j) {
			assertEquals(sourceList.get(i), list.get(j));
		}
	}
	
	@Test
	public void testCreateMap() {
		Map<String,Integer> sourceMap = new HashMap<String,Integer>();
		sourceMap.put("One", 1);
		sourceMap.put("Two", 2);
		sourceMap.put("Three", 3);
		Enumerable<Map.Entry<String,Integer>> enumerable = Enumerable.create(sourceMap);
		Map<String,Integer> resultMap = new HashMap<String,Integer>();
		for (Map.Entry<String,Integer> entry : enumerable) {
			resultMap.put(entry.getKey(), entry.getValue());
		}
		for (String sourceKey : sourceMap.keySet()) {
			assertTrue(resultMap.containsKey(sourceKey));
		}
		for (String resultKey : resultMap.keySet()) {
			assertTrue(sourceMap.containsKey(resultKey));
		}
		for (Map.Entry<String, Integer> sourceEntry : sourceMap.entrySet()) {
			assertEquals(resultMap.get(sourceEntry.getKey()), sourceEntry.getValue());
		}
	}
	
	@Test
	public void testSelectIdentity() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		int i = 0;
		for (Integer value : Enumerable.create(sourceArray).select(new IdentitySelector<Integer>())) {
			assertEquals(sourceArray[i++], value);
		}
	}
	
	@Test
	public void testSelectProject() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		int i = 0;
		for (String value : Enumerable.create(sourceArray).select(new ToStringSelector<Integer>())) {
			assertEquals(sourceArray[i++].toString(), value);
		}
	}
	
	@Test
	public void testWhereAllTrue() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return true;
			}
			
		});
		assertArrayEquals(sourceArray, enumerable.toList().toArray(new Integer[] {}));
	}
	
	@Test
	public void testWhereAllFalse() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return false;
			}
			
		});
		assertFalse(enumerable.hasNext());
	}
	
	@Test
	public void testWhereFirstTrue() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return arg == 1;
			}
			
		});
		List<Integer> resultList = enumerable.toList();
		assertEquals(resultList.size(), 1);
		assertEquals(resultList.get(0).intValue(), 1);
	}
	
	@Test
	public void testWhereMiddleTrue() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return arg == 3;
			}
			
		});
		List<Integer> resultList = enumerable.toList();
		assertEquals(resultList.size(), 1);
		assertEquals(resultList.get(0).intValue(), 3);
	}
	
	@Test
	public void testWhereLastTrue() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return arg == 5;
			}
			
		});
		List<Integer> resultList = enumerable.toList();
		assertEquals(resultList.size(), 1);
		assertEquals(resultList.get(0).intValue(), 5);
	}
	
	@Test
	public void testWhereFirstAndLastTrue() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return arg == 1 || arg == 5;
			}
			
		});
		List<Integer> resultList = enumerable.toList();
		assertEquals(resultList.size(), 2);
		assertEquals(resultList.get(0).intValue(), 1);
		assertEquals(resultList.get(1).intValue(), 5);
	}
	
	@Test
	public void testWhereFirstAndLastFalse() {
		Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
		Enumerable<Integer> enumerable = Enumerable.create(sourceArray).where(new UnaryFunction<Integer, Boolean>() {

			@Override public Boolean invoke(Integer arg) {
				return !(arg == 1 || arg == 5);
			}
			
		});
		List<Integer> resultList = enumerable.toList();
		assertEquals(resultList.size(), 3);
		assertEquals(resultList.get(0).intValue(), 2);
		assertEquals(resultList.get(1).intValue(), 3);
		assertEquals(resultList.get(2).intValue(), 4);
	}
	
	@Test
	public void testAllOnEmpty() {
		Integer[] sourceArray = new Integer[] {};
		assertTrue(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return false;
			}
		}));
	}
	
	@Test
	public void testAllOnOneTrue() {
		Integer[] sourceArray = new Integer[] {1};
		assertTrue(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg == 1;
			}
		}));
	}
	
	@Test
	public void testAllOnOneFalse() {
		Integer[] sourceArray = new Integer[] {1};
		assertFalse(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg != 1;
			}
		}));
	}
	
	@Test
	public void testAllOnManyTrue() {
		Integer[] sourceArray = new Integer[] {1, 2, 3, 4};
		assertTrue(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg > 0;
			}
		}));
	}
	
	@Test
	public void testAllOnManyFalse() {
		Integer[] sourceArray = new Integer[] {1, 2, 3, 4};
		assertFalse(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg < 0;
			}
		}));
	}
	
	@Test
	public void testAllOnSomeFalse() {
		Integer[] sourceArray = new Integer[] {1, 2, 3, 4};
		assertFalse(Enumerable.create(sourceArray).all(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg < 3;
			}
		}));
	}
	
	@Test
	public void testAnyOnEmpty() {
		Integer[] sourceArray = new Integer[] {};
		assertFalse(Enumerable.create(sourceArray).any(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return false;
			}
		}));
	}
	
	@Test
	public void testAnyOnOneTrue() {
		Integer[] sourceArray = new Integer[] {1};
		assertTrue(Enumerable.create(sourceArray).any(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg == 1;
			}
		}));
	}
	
	@Test
	public void testAnyOnOneFalse() {
		Integer[] sourceArray = new Integer[] {1};
		assertFalse(Enumerable.create(sourceArray).any(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg != 1;
			}
		}));
	}
	
	@Test
	public void testAnyOnManyTrue() {
		Integer[] sourceArray = new Integer[] {1, 2, 3, 4};
		assertTrue(Enumerable.create(sourceArray).any(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg == 3;
			}
		}));
	}
	
	@Test
	public void testAnyOnManyFalse() {
		Integer[] sourceArray = new Integer[] {1, 2, 3, 4};
		assertFalse(Enumerable.create(sourceArray).any(new UnaryFunction<Integer,Boolean>() {
			@Override public Boolean invoke(Integer arg) {
				return arg < 0;
			}
		}));
	}
}
