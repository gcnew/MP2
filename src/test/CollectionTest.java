package test;

import java.util.List;
import java.util.ListIterator;

import com.kleverbeast.dpf.common.operationparser.collection.AbstractConsList;

public class CollectionTest extends MP2Test {
	public void testConsBackward0() {
		// highly inefficient.. but anyway
		@SuppressWarnings("boxing")
		final ListIterator<Integer> it = AbstractConsList.<Integer> list(1, 2, 3, 4, 5).listIterator();

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

	public void testConsBackward1() {
		// highly inefficient.. but anyway
		@SuppressWarnings("boxing")
		final List<Integer> list = AbstractConsList.<Integer> list(1, 2, 3, 4, 5);
		final ListIterator<Integer> it = list.listIterator(list.size());

		int i = 5;
		while (it.hasPrevious()) {
			assertEquals(i - 1, it.previousIndex());
			assertEquals(i--, it.previous().intValue());
		}
		assertTrue(it.previousIndex() < 0);
	}
}
