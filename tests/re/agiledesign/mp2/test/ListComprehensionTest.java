package re.agiledesign.mp2.test;

public class ListComprehensionTest extends MP2Test {
	public void testFictorial() {
		final String script = //
		/**/"fac_memo = ( (0, 1) : ( (x[0] + 1, x[1] * (x[0] + 1) ); x = fac_memo ) )\n" +
		/**/"fac = (n) => return fac_memo[n][1]\n" +
		/**/"\n" +
		/**/"return fac(5)";
		// http://codepad.org/SL4PWd2V
		// http://codepad.org/Gn3t9Zpz

		assertEval(script, Integer.valueOf(120));
	}
}
