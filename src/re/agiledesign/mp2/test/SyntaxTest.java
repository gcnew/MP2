package re.agiledesign.mp2.test;

import re.agiledesign.mp2.exception.ParsingException;

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

	public void testNumericLiterals() throws Exception {
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
}
