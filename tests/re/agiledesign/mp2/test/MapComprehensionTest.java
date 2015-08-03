package re.agiledesign.mp2.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class MapComprehensionTest extends MP2Test {
	@Test
	public void immutable0() {
		final Map<Integer, String> expected = new HashMap<Integer, String>();
		expected.put(Integer.valueOf(1), "a");
		expected.put(Integer.valueOf(2), "b");
		expected.put(Integer.valueOf(3), "c");

		assertEval("return ( 1 -> 'a', 2 -> 'b', 3 -> 'c' )", expected);
	}

	@Test
	public void immutable1() {
		assertEval("return ( 1 -> 'a', 2 -> 'b', 3 -> 'c' )[2]", "b");
	}

	@Test
	public void immutable2() {
		final Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("a", Integer.valueOf(1));
		expected.put("b", Integer.valueOf(2));

		assertEval("return ( 1 -> ( 'a' -> 1, 'b' -> 2 ), 2 -> 'b' )[1]", expected);
	}

	@Test
	public void immutable3() {
		final String script = //
		/**/"a = 'abcd'\n" +
		/**/"return ( a -> () => return 7 )[a]()";

		assertEval(script, Integer.valueOf(7));
	}

	@Test
	public void immutable4() {
		assertEval("return ( 'q' -> (1, 2, 3,), )['q'][0]", Integer.valueOf(1));
	}

	@Test
	public void immutable5() {
		assertException("( 'q' -> (1, 2, 3,), ).put('p', 0)", Exception.class);
	}

	@Test
	@Ignore
	public void immutable6() {
		final String script = "return ( 'found' -> x.getValue(); x = ( 1 -> 'a', 2 -> 'b', 3 -> 'c' ); x.getKey() == 1 )";
		assertEval(script, Collections.singletonMap("found", "a"));
	}

	@Test
	public void mutable0() {
		final Map<Integer, String> expected = new HashMap<Integer, String>();
		expected.put(Integer.valueOf(1), "a");
		expected.put(Integer.valueOf(2), "b");
		expected.put(Integer.valueOf(3), "c");

		assertEval("return [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ]", expected);
	}

	@Test
	public void mutable1() {
		assertEval("return [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ][2]", "b");
	}

	@Test
	public void mutable2() {
		final Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("a", Integer.valueOf(1));
		expected.put("b", Integer.valueOf(2));

		assertEval("return [ 1 -> [ 'a' -> 1, 'b' -> 2 ], 2 -> 'b' ][1]", expected);
	}

	@Test
	public void mutable3() {
		final String script = //
		/**/"a = 'abcd'\n" +
		/**/"return [ a -> () => return 7 ][a]()";

		assertEval(script, Integer.valueOf(7));
	}

	@Test
	public void mutable4() {
		assertEval("return [ 'q' -> [1, 2, 3,], ]['q'][0]", Integer.valueOf(1));
	}

	@Test
	public void mutable5() {
		final String script = //
		/**/"a = [ 'q' -> [1, 2, 3,], ]\n" +
		/**/"a.put('p', 0)\n" +
		/**/"return a.size();";

		assertEval(script, Integer.valueOf(2));
	}

	@Test
	@Ignore
	public void mutable6() {
		final String script = "return [ 'found' -> x.getValue(); x = [ 1 -> 'a', 2 -> 'b', 3 -> 'c' ]; x.getKey() == 1 ]";
		assertEval(script, Collections.singletonMap("found", "a"));
	}
}
