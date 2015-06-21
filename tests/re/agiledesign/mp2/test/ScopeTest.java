package re.agiledesign.mp2.test;

import re.agiledesign.mp2.exception.ParsingException;

public class ScopeTest extends MP2Test {
	private static final Integer EXPECTED = Integer.valueOf(6);

	public void testRootLocal() {
		final String script = //
		/**/"local i = 5;\n" +
		/**/"local f = () => return 1;\n" +
		/**/"return f() + i;";

		assertEval(script, EXPECTED);
	}

	public void testMultipleLocals() {
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

	public void testLocalInLoop() {
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

	public void testVarSameAsLocal() {
		final String script = //
		/**/"local i = 5;\n" +
		/**/"var   i = 3;";

		// var names should not collide with local variables names
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
