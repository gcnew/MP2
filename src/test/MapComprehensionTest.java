package test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapComprehensionTest extends MP2Test {
	public void testImmutable0() throws Exception {
		final Map<Integer, String> expected = new HashMap<Integer, String>();
		expected.put(Integer.valueOf(1), "a");
		expected.put(Integer.valueOf(2), "b");
		expected.put(Integer.valueOf(3), "c");

		assertEval("return ( 1 -> 'a', 2 -> 'b', 3 -> 'c' )", expected);
	}

	public void testImmutable1() throws Exception {
		assertEval("return ( 1 -> 'a', 2 -> 'b', 3 -> 'c' )[2]", "b");
	}

	public void testImmutable2() throws Exception {
		final Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("a", Integer.valueOf(1));
		expected.put("b", Integer.valueOf(2));

		assertEval("return ( 1 -> ( 'a' -> 1, 'b' -> '2' ), 2 -> 'b' )[1]", expected);
	}

	public void testImmutable3() throws Exception {
		final String script = //
		/**/"a = 'abcd'\n" +
		/**/"return ( a -> () => return 7 )[a]()";

		assertEval(script, Integer.valueOf(7));
	}

	public void testImmutable4() throws Exception {
		assertEval("return ( 'q' -> (1, 2, 3,), )['q'][0]", Integer.valueOf(1));
	}

	public void testImmutable5() throws Exception {
		assertException("( 'q' -> (1, 2, 3,), ).put('q', 0)", Exception.class);
	}

	public void testImmutable6() throws Exception {
		final String script = "return ( 'found' -> x.getValue(); x = ( 1 -> 'a', 2 -> 'b', 3 -> 'c' ); x.getKey() == 1 )";
		assertEval(script, Collections.singletonMap("found", "a"));
	}

	public void testMutable0() throws Exception {
		final Map<Integer, String> expected = new HashMap<Integer, String>();
		expected.put(Integer.valueOf(1), "a");
		expected.put(Integer.valueOf(2), "b");
		expected.put(Integer.valueOf(3), "c");

		assertEval("return [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ]", expected);
	}

	public void testMutable1() throws Exception {
		assertEval("return [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ][2]", "b");
	}

	public void testMutable2() throws Exception {
		final Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("a", Integer.valueOf(1));
		expected.put("b", Integer.valueOf(2));

		assertEval("return [ 1 -> [ 'a' -> 1, 'b' -> '2' ], 2 -> 'b' ][1]", expected);
	}

	public void testMutable3() throws Exception {
		final String script = //
		/**/"a = 'abcd'\n" +
		/**/"return [ a -> () => return 7 ][a]()";

		assertEval(script, Integer.valueOf(7));
	}

	public void testMutable4() throws Exception {
		assertEval("return [ 'q' -> [1, 2, 3,], ]['q'][0]", Integer.valueOf(1));
	}

	public void testMutable5() throws Exception {
		assertEval("return [ 'q' -> [1, 2, 3,], ].put('q', 0).size()", Integer.valueOf(2));
	}

	public void testMutable6() throws Exception {
		final String script = "return [ 'found' -> x.getValue(); x = [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ]; x.getKey() == 1 ]";
		assertEval(script, Collections.singletonMap("found", "a"));
	}
}
