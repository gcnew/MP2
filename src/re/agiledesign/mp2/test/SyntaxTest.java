package re.agiledesign.mp2.test;

public class SyntaxTest extends MP2Test {
	public void testTernary0() throws Exception {
		assertEval("return ((a = true) ? a : false) ? 4 : 5", Integer.valueOf(4));
	}

	public void testTernary1() throws Exception {
		assertEval("return (a = true) ? a : false ? 4 : 5", Boolean.TRUE);
	}

	public void testTernary2() throws Exception {
		assertEval("return a = false ? a : false ? 4 : 5", Integer.valueOf(5));
	}

	public void testTernaryPrecedence0() throws Exception {
		assertEval("return a = false ? 1 : 2", Integer.valueOf(2));
	}
}
