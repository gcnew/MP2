package re.agiledesign.mp2.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import re.agiledesign.mp2.exception.ParsingException;

public class FunctionTest extends MP2Test {
	public void testLambda0() {
		assertEval("x = (x) => return 1 + x return x(9)", Integer.valueOf(10));
	}

	public void testLambda1() {
		assertEval("return !(x) => { return 1 + x }(9)", Boolean.FALSE);
	}

	public void testLambda2() {
		assertEval("return ((x) => return 1 + x)(9)", Integer.valueOf(10));
	}

	public void testLambda3() {
		assertEval("return (() => return 10)(1, 2, 3)", Integer.valueOf(10));
	}

	public void testImmediate() {
		final String script = //
		/**/"test = () => return [ () => return (1 .. 2) ]\n" +
		/**/"return test()[0]().size()";

		assertEval(script, Integer.valueOf(2));
	}

	public void testNested() {
		final String script = //
		/**/"test = () => return () => return (1 .. 2)\n" +
		/**/"return test()().size()";

		assertEval(script, Integer.valueOf(2));
	}

	public void testLambda() {
		final String script = //
		/**/"x = 2\n" +
		/**/"addLambda = (x, y) => return () => return x + y + 1\n" +
		/**/"return addLambda(4, 5)()";

		assertEval(script, Integer.valueOf(10));
	}

	public void testHigherOrder() {
		final String script = //
		/**/"addOne = (x) => return 1 + x\n" +
		/**/"higherOrder = (x, f) => return f(x)\n" +
		/**/"return higherOrder(9, addOne)";

		assertEval(script, Integer.valueOf(10));
	}

	@SuppressWarnings("boxing")
	public void testRecursiveMap() {
		final String script = //
		/**/"car = (l) => return !l || l.isEmpty() ? null : l[0]\n" +
		/**/"cdr = (l) => return !l || (l.size() < 2) ? null : l[1 -> l.size()]\n" +
		/**/"cons = (x, l) => { local retval = [x]; retval.addAll(l); return retval }\n" +
		/**/"map = (l, f) => return !l && [] || cons(f(car(l)), map(cdr(l), f))\n" +
		/**/"mul2 = (x) => return x * 2\n" +
		/**/"return map([1 .. 3], mul2)";

		assertEval(script, Arrays.asList(2, 4, 6));
	}

	@SuppressWarnings("boxing")
	public void testRecursiveMap2() {
		final String script = //
		/**/"car = (l) => return l === null ? null : l.first()\n" +
		/**/"cdr = (l) => return l === null ? null : l.rest()\n" +
		/**/"cons = (x, l) => return (x : l)\n" +
		/**/"tcl = (l) => return (null : l).rest()\n" +
		/**/"map = (l, f) => return l === null ? null : cons(f(car(l)), map(cdr(l), f))\n" +
		/**/"mul2 = (x) => return x * 2\n" +
		/**/"return map(tcl((1 .. 3)), mul2)";

		assertEval(script, Arrays.asList(2, 4, 6));
	}

	@SuppressWarnings({ "unchecked", "boxing" })
	public void testFilter() {
		final String script = //
		/**/"function filter(l, f) {\n" +
		/**/"	local i, retval = []\n" +
		/**/"\n" +
		/**/"	for (i = 0; i < l.size(); i = i + 1)\n" +
		/**/"		if (f(l[i]))\n" +
		/**/"			retval.add(l[i])\n" +
		/**/"\n" +
		/**/"	return retval\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"l = [1, 10, 100, 1000]\n" +
		/**/"return [\n" +
		/**/"	filter(l, (x) => return x > 0),\n" +
		/**/"	filter(l, (x) => return x > 10),\n" +
		/**/"	filter(l, (x) => return x > 1000)\n" +
		/**/"]";

		final List<Integer> list0 = Arrays.asList(1, 10, 100, 1000);
		final List<Integer> list1 = Arrays.asList(100, 1000);
		final List<Integer> list2 = Collections.emptyList();

		assertEval(script, Arrays.<List<Integer>> asList(list0, list1, list2));
	}

	public void testLocalSameAsClosed() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	var i = 3;\n" +
		/**/"\n" +
		/**/"	return () => { i = i + 1; local i = 5; return i; }\n" +
		/**/"}";

		// locals/vars names should not collide with used closed variables names
		assertException(script, ParsingException.class);
	}

	public void testArgumentSameAsClosed() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	var i = 3;\n" +
		/**/"\n" +
		/**/"	return (i) => { return i; }\n" +
		/**/"}";

		// argument names should not collide with closed variables names
		assertException(script, ParsingException.class);
	}

	public void testArgumentSameAsLocal() {
		final String script = //
		/**/"function test(i) {\n" +
		/**/"	local i = 3;\n" +
		/**/"}";

		// argument names should not collide with local variables names
		assertException(script, ParsingException.class);
	}
}
