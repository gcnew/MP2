package re.agiledesign.mp2.test;

import java.util.Collections;

public class PrimitivesTest extends MP2Test {
	public void testPrimitive0() throws Exception {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0].toString()", Collections.<String, Object> singletonMap("a", ints), "1");
	}

	public void testPrimitive1() throws Exception {
		final int ints[] = { 1, 2, 3, 4, 5 };

		assertEval("return a[0] == 1", Collections.<String, Object> singletonMap("a", ints), Boolean.TRUE);
	}

	public void testPrimitive2() throws Exception {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0() == 0", Collections.<String, Object> singletonMap("a", a), Boolean.TRUE);
	}

	public void testPrimitive3() throws Exception {
		final Object a = new Object() {
			@SuppressWarnings("unused")
			public int return0() {
				return 0;
			}
		};

		assertEval("return a.return0().toString()", Collections.<String, Object> singletonMap("a", a), "0");
	}
}
