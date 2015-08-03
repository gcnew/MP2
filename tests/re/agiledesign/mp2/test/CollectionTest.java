package re.agiledesign.mp2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.junit.Test;

import re.agiledesign.mp2.collection.ConsList;

public class CollectionTest extends MP2Test {
	@Test
	public void consBackward0() {
		// highly inefficient.. but anyway
		@SuppressWarnings("boxing")
		final ListIterator<Integer> it = ConsList.<Integer> list(1, 2, 3, 4, 5).listIterator();

		int i = 0;
		while (it.hasNext()) {
			assertEquals(i, it.nextIndex());
			assertEquals(++i, it.next().intValue());
		}
		assertTrue(it.nextIndex() == 5);

		while (it.hasPrevious()) {
			assertEquals(i - 1, it.previousIndex());
			assertEquals(i--, it.previous().intValue());
		}
		assertTrue(it.previousIndex() < 0);

		it.next();
		assertTrue(it.next() == it.previous());
	}

	@Test
	public void consBackward1() {
		// highly inefficient.. but anyway
		@SuppressWarnings("boxing")
		final List<Integer> list = ConsList.<Integer> list(1, 2, 3, 4, 5);
		final ListIterator<Integer> it = list.listIterator(list.size());

		int i = 5;
		while (it.hasPrevious()) {
			assertEquals(i - 1, it.previousIndex());
			assertEquals(i--, it.previous().intValue());
		}
		assertTrue(it.previousIndex() < 0);
	}

	private static void executeEquals(final Object aFirst, final Object aSecond) {
		final Map<String, Object> m = new HashMap<String, Object>();
		m.put("a", aFirst);
		m.put("b", aSecond);

		assertEval("return a == b", m, Boolean.TRUE);
	}

	@Test
	public void listEquals() {
		@SuppressWarnings("boxing")
		final List<?> a = Arrays.asList(1, 2, 3);
		final List<?> b = new ArrayList<Object>(a);

		executeEquals(a, b);
	}

	@Test
	public void nestedListEquals() {
		@SuppressWarnings("boxing")
		final List<?> a = Arrays.asList(1, 2, 3);
		final List<?> b = new ArrayList<Object>(a);

		executeEquals(Arrays.<List<?>> asList(a), Arrays.<List<?>> asList(b));
	}

	@Test
	public void listContainingArrayEquals() {
		final int arr[] = new int[] { 1, 2, 3 };

		final List<?> a = Arrays.asList(arr);
		final List<Object> b = new ArrayList<Object>();
		b.add(arr.clone());

		executeEquals(a, b);
	}

	@Test
	public void listArrayEquals() {
		@SuppressWarnings("boxing")
		final List<?> a = Arrays.asList(1, 2, 3);
		final int b[] = new int[] { 1, 2, 3 };

		executeEquals(a, b);
	}
}
