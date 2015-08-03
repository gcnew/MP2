package re.agiledesign.mp2.test;

import org.junit.Test;

import re.agiledesign.mp2.exception.ParsingException;

public class ScopeTest extends MP2Test {
	private static final Integer EXPECTED = Integer.valueOf(6);

	@Test
	public void rootLocal() {
		final String script = //
		/**/"local i = 5;\n" +
		/**/"local f = () => return 1;\n" +
		/**/"return f() + i;";

		assertEval(script, EXPECTED);
	}

	@Test
	public void multipleLocals() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	local i = 2, z, k = 1;\n" +
		/**/"	z = 3;\n" +
		/**/"	return i + k + z + 0;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test()";

		assertEval(script, EXPECTED);
	}

	@Test
	public void localInLoop() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	local i = 0, sum = 0;\n" +
		/**/"	for (i = 1; i <= 3; i = i + 1) {\n" +
		/**/"		local k = i;\n" +
		/**/"		sum += k;\n" +
		/**/"	}\n" +
		/**/"\n" +
		/**/"	return sum;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test()";

		assertEval(script, EXPECTED);
	}

	@Test
	public void localSameAsClosed() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	var i = 3;\n" +
		/**/"\n" +
		/**/"	return () => { i = i + 1; local i = 5; return i; }\n" +
		/**/"}";

		// locals/vars names should not collide with used closed variables names
		assertException(script, ParsingException.class);
	}

	@Test
	public void nameOverlap() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	local i = 3;\n" +
		/**/"\n" +
		/**/"	return () => { local i = 5; return i; }\n" +
		/**/"}";

		// variable names should not overlap
		assertException(script, ParsingException.class);
	}

	@Test
	public void functionSameAsLocal() {
		final String script = //
		/**/"local test = true;\n" +
		/**/"function test() { return true; };";

		assertException(script, ParsingException.class);
	}

	@Test
	public void functionSameAsLocal2() {
		final String script = //
		/**/"function test() { return true; };\n" +
		/**/"var test = true;\n";

		assertException(script, ParsingException.class);
	}

	@Test
	public void argumentSameAsClosed() {
		final String script = //
		/**/"function test() {\n" +
		/**/"	var i = 3;\n" +
		/**/"\n" +
		/**/"	return (i) => { return i; }\n" +
		/**/"}";

		// argument names should not collide with closed variables names
		assertException(script, ParsingException.class);
	}

	@Test
	public void varSameAsLocal() {
		final String script = //
		/**/"local i = 5;\n" +
		/**/"var   i = 3;";

		// var names should not collide with local variables names
		assertException(script, ParsingException.class);
	}

	@Test
	public void argumentSameAsLocal() {
		final String script = //
		/**/"function test(i) {\n" +
		/**/"	local i = 3;\n" +
		/**/"}";

		// argument names should not collide with local variables names
		assertException(script, ParsingException.class);
	}
}
