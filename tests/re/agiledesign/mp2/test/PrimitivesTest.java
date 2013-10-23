package re.agiledesign.mp2.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.util.ArrayUtil;

public class PrimitivesTest extends MP2Test {
	public void testPrimitive0() {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0].toString()", Collections.singletonMap("a", ints), "1");
	}

	public void testPrimitive1() {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0] == 1", Collections.singletonMap("a", ints), Boolean.TRUE);
	}

	public void testPrimitive2() {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0() == 0", Collections.singletonMap("a", a), Boolean.TRUE);
	}

	public void testPrimitive3() {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0().toString()", Collections.singletonMap("a", a), "0");
	}

	public void testArrayEquals() {
		final int a[][] = { { 0 } };
		final int b[][] = { { 0 } };

		final Map<String, Object> m = new HashMap<String, Object>();
		m.put("a", a);
		m.put("b", b);

		assertTrue(ArrayUtil.equals(a, b));
		assertEval("return a == b", m, Boolean.TRUE);
	}
}
