package re.agiledesign.mp2.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import re.agiledesign.mp2.Interpreter;
import re.agiledesign.mp2.InterpreterFactory;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.expressions.FunctionExpression;
import re.agiledesign.mp2.internal.sourceprovider.NullProvider;

public class SyntaxTest extends MP2Test {
	private static final Integer NINE = Integer.valueOf(9);

	@Test
	public void ternary0() {
		assertEval("return ((a = true) ? a : false) ? 4 : 5", Integer.valueOf(4));
	}

	@Test
	public void ternary1() {
		assertEval("return (a = true) ? a : false ? 4 : 5", Boolean.TRUE);
	}

	@Test
	public void ternary2() {
		assertEval("return a = false ? a : false ? 4 : 5", Integer.valueOf(5));
	}

	@Test
	public void ternaryPrecedence0() {
		assertEval("return a = false ? 1 : 2", Integer.valueOf(2));
	}

	@Test
	public void numericLiterals() {
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

	@Test
	public void casts() {
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

	@Test
	public void coercion() {
		assertEval("return '' + null", "null");
		assertEval("return '3' + 4", "34");
		assertEval("return 3.5 + '4'", "3.54");
		assertEval("return 3.5 + 4 + ''", "7.5");
	}

	@Test
	public void string() {
		assertEval("return 'Say \\'Hello\\''", "Say 'Hello'");
		assertEval("return \"Say 'Hello'\"", "Say 'Hello'");
		assertEval("return 'Say \"Hello\"'", "Say \"Hello\"");
		assertEval("return \"Say \\\"Hello\\\"\"", "Say \"Hello\"");
		assertException("'Say \\", ParsingException.class);
	}

	@Test
	public void complexIncrement() {
		assertEval("local i = 0; local a = [[1]]; return a[i++][--i] += i + 8;", NINE);
	}

	@Test
	public void varPreIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("a", Integer.valueOf(8));

		assertEval("return ++a", a, NINE);
	}

	@Test
	public void varPostDecrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("a", NINE);

		assertEval("return a--", a, NINE);
	}

	@Test
	public void primitiveArrayAssignment() {
		assertEval("return a[0]= 3", Collections.singletonMap("a", new int[] { 0, 1, 2 }), Integer.valueOf(3));
	}

	@Test
	public void primitiveArrayAssignment2() {
		final int arr[] = { 0, 1, 2 };
		final int expected[] = { 3, 1, 2 };

		assertEval("a[0]= 3; return a", Collections.singletonMap("a", arr), expected, ArrayEquals);
	}

	@Test
	public void primitiveArrayAssignment3() {
		// TODO: should we do coercion or simply fail?
		assertEval("return a[0]= 3.0", Collections.singletonMap("a", new int[] { 0, 1, 2 }), Integer.valueOf(3));
	}

	@Test
	public void primitiveArrayAssignment4() {
		assertException(
			"return a[0]= a",
			Collections.singletonMap("a", new int[] { 0, 1, 2 }),
			ClassCastException.class);
	}

	@Test
	public void listAssignment() {
		final List<String> list = new ArrayList<String>();
		list.add("empty");

		assertEval("l[0] = 'abcd'; return l", Collections.singletonMap("l", list), Collections.singletonList("abcd"));
	}

	@Test
	public void listAssignment2() {
		final List<String> list = new ArrayList<String>();
		list.add("empty");

		assertEval("return l[0] = 'abcd'", Collections.singletonMap("l", list), "abcd");
	}

	@SuppressWarnings("boxing")
	@Test
	public void listAssignment3() {
		assertEval("l = [ 0, 1, 2 ]; l[0] = 3; return l", Arrays.asList(3, 1, 2));
	}

	@Test
	public void listAssignment4() {
		assertException("l = ( 0, 1, 2 ); l[0] = 3", UnsupportedOperationException.class);
	}

	@Test
	public void listComplexEquals() {
		final List<Integer> a = Arrays.asList(Integer.valueOf(0));

		assertEval("return a[0] += 9", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get(0));
	}

	@Test
	public void listPreDecrement() {
		final List<Integer> a = Arrays.asList(Integer.valueOf(10));

		assertEval("return --a[0]", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get(0));
	}

	@Test
	public void listPostDecrement() {
		final List<Integer> a = Arrays.asList(NINE);

		assertEval("return a[0]--", Collections.singletonMap("a", a), NINE);
		assertEquals(Integer.valueOf(8), a.get(0));
	}

	@Test
	public void mapAssignment() {
		final Map<Integer, String> expected = Collections.singletonMap(Integer.valueOf(0), "abcd");
		final Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Integer.valueOf(0), "empty");

		assertEval("m[0] = 'abcd'; return m", Collections.singletonMap("m", map), expected);
	}

	@Test
	public void mapAssignment2() {
		final Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Integer.valueOf(0), "empty");

		assertEval("return m[0] = 'abcd'", Collections.singletonMap("m", map), "abcd");
	}

	@Test
	public void mapAssignment3() {
		assertEval("l = [ 'a' -> 'b' ]; l['a'] = 'a'; return l", Collections.singletonMap("a", "a"));
	}

	@Test
	public void mapAssignment4() {
		assertException("l = ( 'a' -> 'b' ); l['a'] = 'a'; return l", UnsupportedOperationException.class);
	}

	@Test
	public void mapComplexEquals() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", Integer.valueOf(0));

		assertEval("return a['i'] += 9", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get("i"));
	}

	@Test
	public void mapPreIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", Integer.valueOf(8));

		assertEval("return ++a['i']", Collections.singletonMap("a", a), NINE);
		assertEquals(NINE, a.get("i"));
	}

	@Test
	public void mapPostIncrement() {
		final Map<String, Integer> a = new HashMap<String, Integer>();
		a.put("i", NINE);

		assertEval("return a['i']++", Collections.singletonMap("a", a), NINE);
		assertEquals(Integer.valueOf(10), a.get("i"));
	}

	@Test
	public void nestedAssignment() {
		assertEval("return a[0][0] = 3", Collections.singletonMap("a", new int[][] { { 0 } }), Integer.valueOf(3));
	}

	@Test
	public void nestedAssignment2() {
		final Map<String, Object> m = new HashMap<String, Object>();
		final List<Integer> l = new ArrayList<Integer>();

		l.add(Integer.valueOf(0));
		m.put("l", l);

		eval("m['l'][0] = 3", Collections.singletonMap("m", m));
		assertEquals(Integer.valueOf(3), l.get(0));
	}

	@Test
	public void globalAssignment() throws Exception {
		final Interpreter interpreter = InterpreterFactory.getInstance(NullProvider.instance(), null);

		interpreter.eval("function f() { }");
		interpreter.eval("g = () => { }");
		interpreter.eval("x = 9");

		assertTrue(interpreter.getGlobal("f") instanceof FunctionExpression);
		assertTrue(interpreter.getGlobal("g") instanceof FunctionExpression);
		assertEquals(interpreter.getGlobal("x"), NINE);
	}

	@Test
	public void varAssignment() {
		assertEval("var i = 9; return i;", NINE);
	}

	@Test
	public void varAssignment2() {
		assertEval("local l = 9; var v = 10; return l;", NINE);
	}

	@Test
	public void localAssignment() {
		assertEval("local i = 9; return i;", NINE);
	}

	@Test
	public void argumentAssignment() {
		assertEval("return (i) => { i = 9; return i; }()", NINE);
	}

	@Test
	@Ignore
	public void closureAssignment() {
		// TODO: should we allow closure assignment or disable it altogether?
		assertEval("var i = 0; (() => return i = 9)(); return i;", NINE);
	}

	@Test
	@Ignore
	public void closureUpdate() {
		final String script = //
		/**/"var n = 4;\n" +
		/**/"local test = () => return n + 4;\n" +
		/**/"n = 5;\n" +
		/**/"\n" +
		/**/"return test()";

		assertEval(script, NINE);
	}

	@Test
	public void argumentAssignment2() {
		final String script = //
		/**/"function test(aInteger) {\n" +
		/**/"	aInteger = 6\n;" +
		/**/"	return aInteger;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test() + ((x) => return x = 3;)(8)";

		assertEval(script, NINE);
	}

	@Test
	@SuppressWarnings("boxing")
	public void assignmentOrder() {
		assertEval("i = 0; a = [ 3, 6 ]; a[i] = i = 9; return [ i, a[0], a[1] ]", Arrays.asList(9, 9, 6));
	}

	@Test
	public void breakContinueOutsideOfContext() {
		assertException("break", ParsingException.class);
		assertException("continue", ParsingException.class);
		assertException("while (false); break", ParsingException.class);
		assertException("do () => break; while (false);", ParsingException.class);
	}

	@Test
	public void breakContinue() {
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

	@Test
	public void varAssignBeforeUse() {
		assertEval("var a = 4, b = a + 5; return b;", NINE);
		assertException("var a = 5, b += a", ParsingException.class);
		assertException("var a = 5, b; b += a", ParsingException.class);
		assertException("var a; return ++a;", ParsingException.class);
		assertException("var a; return a.toString();", ParsingException.class);

		// TODO: depends on closure implementation
		// assertException("var a; return () => return ++a;()", ParsingException.class);
	}

	@Test
	public void nestedBlocks() {
		assertEval("{{ return 9 }}", NINE);
		assertEval("while (true) {{ return 9 }}", NINE);
	}

	@Test
	public void booleanCoersion() {
		assertEval("local a = null; a ||= 9; return a", NINE);
		assertEval("local a = true; return a &&= 9;", NINE);
	}
}
