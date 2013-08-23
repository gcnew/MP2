package re.agiledesign.mp2.test;

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

	public void testArgumentAssignment() {
		final String script = //
		/**/"function test(aInteger) {\n" +
		/**/"	aInteger = 3\n;" +
		/**/"	return aInteger;\n" +
		/**/"}\n" +
		/**/"\n" +
		/**/"return test() + ((x) => return x = 3;)(8)";

		assertEval(script, EXPECTED);
	}
}
