package re.agiledesign.mp2.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.exception.ParsingException;

public class SyntaxTest extends MP2Test {
	private static final Integer NINE = Integer.valueOf(9);

	public void testTernary0() {
		assertEval("return ((a = true) ? a : false) ? 4 : 5", Integer.valueOf(4));
	}

	public void testTernary1() {
		assertEval("return (a = true) ? a : false ? 4 : 5", Boolean.TRUE);
	}

	public void testTernary2() {
		assertEval("return a = false ? a : false ? 4 : 5", Integer.valueOf(5));
	}

	public void testTernaryPrecedence0() {
		assertEval("return a = false ? 1 : 2", Integer.valueOf(2));
	}

	public void testNumericLiterals() {
		final Integer i255 = Integer.valueOf(255);
		assertEval("return 255", i255);
		assertEval("return 0xFF", i255);
		assertEval("return 0d255", i255);
		assertEval("return 0o377", i255);
		assertEval("return 0b11111111", i255);

		final Double d3_0 = Double.valueOf(3.0);
		assertEval("return 3.0", d3_0);
		assertEval("return 3.", d3_0);

		assertEval("return 3.toString()", "3");
		assertEval("return 3.01.toString()", "3.01");

		assertException("0777", ParsingException.class);
		assertException("0dFF", ParsingException.class);
		assertException("0dFF.44", ParsingException.class);
		assertException("1F3.33", ParsingException.class);
	}

	public void testCasts() {
		assertEval("return (int) null", null);
		assertEval("return (int) 123", Integer.valueOf(123));
		assertEval("return (int) 123.1", Integer.valueOf(123));
		assertEval("return (int) '123'", Integer.valueOf(123));

		assertEval("return (long) null", null);
		assertEval("return (long) 123", Long.valueOf(123));
		assertEval("return (long) 123.1", Long.valueOf(123));
		assertEval("return (long) '123'", Long.valueOf(123));

		assertEval("return (byte) null", null);
		assertEval("return (byte) 123", Byte.valueOf((byte) 123));
		assertEval("return (byte) 123.1", Byte.valueOf((byte) 123));
		assertEval("return (byte) '123'", Byte.valueOf((byte) 123));

		assertEval("return (char) null", null);
		assertEval("return (char) 123", Character.valueOf((char) 123));
		assertEval("return (char) 123.1", Character.valueOf((char) 123));
		assertEval("return (char) '123'", Character.valueOf((char) 123));

		assertEval("return (short) null", null);
		assertEval("return (short) 123", Short.valueOf((short) 123));
		assertEval("return (short) 123.1", Short.valueOf((short) 123));
		assertEval("return (short) '123'", Short.valueOf((short) 123));

		assertEval("return (bigint) null", null);
		assertEval("return (bigint) 123", BigInteger.valueOf(123));
		assertEval("return (bigint) 123.1", BigInteger.valueOf(123));
		assertEval("return (bigint) '123'", BigInteger.valueOf(123));

		assertEval("return (float) null", null);
		assertEval("return (float) 123", Float.valueOf(123));
		assertEval("return (float) 123.1", Float.valueOf((float) 123.1));
		assertEval("return (float) '123'", Float.valueOf(123));

		assertEval("return (double) null", null);
		assertEval("return (double) 123", Double.valueOf(123));
		assertEval("return (double) 123.1", Double.valueOf(123.1));
		assertEval("return (double) '123'", Double.valueOf(123));

		assertEval("return (decimal) null", null);
		assertEval("return (decimal) 123", BigDecimal.valueOf(123.0));
		assertEval("return (decimal) 123.1", BigDecimal.valueOf(123.1));
		assertEval("return (decimal) '123'", BigDecimal.valueOf(123));

		assertEval("return (string) null", null);
		assertEval("return (string) '123'", "123");
		assertEval("return (string) 123", String.valueOf(123));
		assertEval("return (string) 123.1", String.valueOf(123.1));
		assertEval("return (string) 123", String.valueOf(123));

		assertEval("return (boolean) null", Boolean.FALSE);
		assertEval("return (boolean) 123", Boolean.TRUE);
		assertEval("return (boolean) 123.1", Boolean.TRUE);
		assertEval("return (boolean) 0", Boolean.FALSE);
		assertEval("return (boolean) 0.1", Boolean.FALSE);
		assertEval("return (boolean) ''", Boolean.TRUE);
		assertEval("return (boolean) 'true'", Boolean.TRUE);
		assertEval("return (boolean) 'false'", Boolean.FALSE);
	}

	public void testCoercion() {
		assertEval("return '' + null", "null");
		assertEval("return '3' + 4", "34");
		assertEval("return 3.5 + '4'", "3.54");
		assertEval("return 3.5 + 4 + ''", "7.5");
	}

	public void testString() {
		assertEval("return 'Say \\'Hello\\''", "Say 'Hello'");
		assertEval("return \"Say 'Hello'\"", "Say 'Hello'");
		assertEval("return 'Say \"Hello\"'", "Say \"Hello\"");
		assertEval("return \"Say \\\"Hello\\\"\"", "Say \"Hello\"");
		assertException("'Say \\", ParsingException.class);
	}

	public void testComplexIncrement() {
		assertEval("local i = 0; local a = [[1]]; return a[i++][--i] += i + 8;", NINE);
	}

	public void testVarPreIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("a", Integer.valueOf(8));

		assertEval("return ++a", a, NINE);
	}

	public void testVarPostDecrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("a", NINE);

		assertEval("return a--", a, NINE);
	}

	public void testPrimitiveArrayAssignment() {
		assertEval("return a[0]= 3", Collections.singletonMap("a", new int[] { 0, 1, 2 }), Integer.valueOf(3));
	}

	public void testPrimitiveArrayAssignment2() {
		final int arr[] = { 0, 1, 2 };
		final int expected[] = { 3, 1, 2 };

		assertEval("a[0]= 3; return a", Collections.singletonMap("a", arr), expected, ArrayEquals);
	}

	public void testPrimitiveArrayAssignment3() {
		// TODO: should we do coercion or simply fail?
		assertEval("return a[0]= 3.0", Collections.singletonMap("a", new int[] { 0, 1, 2 }), Integer.valueOf(3));
	}

	public void testPrimitiveArrayAssignment4() {
		assertException("return a[0]= a",
				Collections.singletonMap("a", new int[] { 0, 1, 2 }),
				ClassCastException.class);
	}

	public void testListAssignment() {
		final List<String> list = new ArrayList<String>();
		list.add("empty");

		assertEval("l[0] = 'abcd'; return l", Collections.singletonMap("l", list), Collections.singletonList("abcd"));
	}

	public void testListAssignment2() {
		final List<String> list = new ArrayList<String>();
		list.add("empty");

		assertEval("return l[0] = 'abcd'", Collections.singletonMap("l", list), "abcd");
	}

	@SuppressWarnings("boxing")
	public void testListAssignment3() {
		assertEval("l = [ 0, 1, 2 ]; l[0] = 3; return l", Arrays.asList(3, 1, 2));
	}

	public void testListAssignment4() {
		assertException("l = ( 0, 1, 2 ); l[0] = 3", UnsupportedOperationException.class);
	}

	public void testListComplexEquals() {
		final List<Integer> a = Arrays.asList(Integer.valueOf(0));

		assertEval("return a[0] += 9", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get(0));
	}

	public void testListPreDecrement() {
		final List<Integer> a = Arrays.asList(Integer.valueOf(10));

		assertEval("return --a[0]", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get(0));
	}

	public void testListPostDecrement() {
		final List<Integer> a = Arrays.asList(NINE);

		assertEval("return a[0]--", Collections.singletonMap("a", a), NINE);
		assertEquals(Integer.valueOf(8), a.get(0));
	}

	public void testMapAssignment() {
		final Map<Integer, String> expected = Collections.singletonMap(Integer.valueOf(0), "abcd");
		final Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Integer.valueOf(0), "empty");

		assertEval("m[0] = 'abcd'; return m", Collections.singletonMap("m", map), expected);
	}

	public void testMapAssignment2() {
		final Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Integer.valueOf(0), "empty");

		assertEval("return m[0] = 'abcd'", Collections.singletonMap("m", map), "abcd");
	}

	public void testMapAssignment3() {
		assertEval("l = [ 'a' -> 'b' ]; l['a'] = 'a'; return l", Collections.singletonMap("a", "a"));
	}

	public void testMapAssignment4() {
		assertException("l = ( 'a' -> 'b' ); l['a'] = 'a'; return l", UnsupportedOperationException.class);
	}

	public void testMapComplexEquals() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", Integer.valueOf(0));

		assertEval("return a['i'] += 9", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get("i"));
	}

	public void testMapPreIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", Integer.valueOf(8));

		assertEval("return ++a['i']", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get("i"));
	}

	public void testMapPostIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", NINE);

		assertEval("return a['i']++", Collections.singletonMap("a", a), NINE);
		assertEquals(Integer.valueOf(10), a.get("i"));
	}

	public void testNestedAssignment() {
		assertEval("return a[0][0] = 3", Collections.singletonMap("a", new int[][] { { 0 } }), Integer.valueOf(3));
	}

	public void testNestedAssignment2() {
		final Map<String, Object> m = new HashMap<String, Object>();
		final List<Integer> l = new ArrayList<Integer>();

		l.add(Integer.valueOf(0));
		m.put("l", l);

		eval("m['l'][0] = 3", Collections.singletonMap("m", m));
		assertEquals(Integer.valueOf(3), l.get(0));
	}

	public void testVarAssignment() {
		assertEval("var i = 9; return i;", NINE);
	}

	public void testVarAssignment2() {
		assertEval("local l = 9; var v = 10; return l;", NINE);
	}

	public void testLocalAssignment() {
		assertEval("local i = 9; return i;", NINE);
	}

	public void testArgumentAssignment() {
		assertEval("return (i) => { i = 9; return i; }()", NINE);
	}

	public void testClosureAssignment() {
		// TODO: should we allow closure assignment or disable it altogether?
		assertEval("var i = 0; (() => return i = 9)(); return i;", NINE);
	}

	public void testFunctionClosure() {
		final String script = //
		/**/"var n = 5;\n" +
		/**/"function test() {\n" +
		/**/"	return n + 4;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test()";

		assertEval(script, NINE);
	}

	public void testClosureUpdate() {
		final String script = //
		/**/"var n = 4;\n" +
		/**/"local test = () => return n + 4;\n" +
		/**/"n = 5;\n" +
		/**/"\n" +
		/**/"return test()";

		assertEval(script, NINE);
	}

	public void testArgumentAssignment2() {
		final String script = //
		/**/"function test(aInteger) {\n" +
		/**/"	aInteger = 6\n;" +
		/**/"	return aInteger;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test() + ((x) => return x = 3;)(8)";

		assertEval(script, NINE);
	}

	@SuppressWarnings("boxing")
	public void testAssignmentOrder() {
		assertEval("i = 0; a = [ 3, 6 ]; a[i] = i = 9; return [ i, a[0], a[1] ]", Arrays.asList(9, 9, 6));
	}

	public void testBreakContinueOutsideOfContext() {
		assertException("break", ParsingException.class);
		assertException("continue", ParsingException.class);
		assertException("while (false); break", ParsingException.class);
	}

	public void testBreakContinue() {
		final String script = //
		/**/"local result = 0;\n" +
		/**/"for (local i = 0; i < 3; ++i) {\n" +
		/**/"	for (var j = 0; ;) {\n;" +
		/**/"		++result;\n" +
		/**/"		if (++j != 3) continue;\n" +
		/**/"		break;\n" +
		/**/"	}\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return result";

		assertEval(script, NINE);
	}
}
