package re.agiledesign.mp2.test;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import re.agiledesign.mp2.util.ArrayUtil;

public class PrimitivesTest extends MP2Test {
	@Test
	public void primitive0() {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0].toString()", Collections.singletonMap("a", ints), "1");
	}

	@Test
	public void primitive1() {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0] == 1", Collections.singletonMap("a", ints), Boolean.TRUE);
	}

	@Test
	public void primitive2() {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0() == 0", Collections.singletonMap("a", a), Boolean.TRUE);
	}

	@Test
	public void primitive3() {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0().toString()", Collections.singletonMap("a", a), "0");
	}

	@Test
	public void arrayEquals() {
		final int a[][] = { { 0 } };
		final int b[][] = { { 0 } };

		final Map<String, Object> m = new HashMap<String, Object>();
		m.put("a", a);
		m.put("b", b);

		assertTrue(ArrayUtil.equals(a, b));
		assertEval("return a == b", m, Boolean.TRUE);
	}
}
