package re.agiledesign.mp2.test;

import java.math.BigDecimal;
import java.math.BigInteger;

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

	public void testCasts() throws Exception {
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

	public void testCoercion() throws Exception {
		assertEval("return '' + null", "null");
		assertEval("return '3' + 4", "34");
		assertEval("return 3.5 + '4'", "3.54");
		assertEval("return 3.5 + 4 + ''", "7.5");
	}

	public void testString() throws Exception {
		assertEval("return 'Say \\'Hello\\''", "Say 'Hello'");
		assertEval("return \"Say 'Hello'\"", "Say 'Hello'");
		assertEval("return 'Say \"Hello\"'", "Say \"Hello\"");
		assertEval("return \"Say \\\"Hello\\\"\"", "Say \"Hello\"");
	}
}
