package re.agiledesign.mp2.test;

import java.util.Arrays;

import re.agiledesign.mp2.exception.ParsingException;

public class RangeListTest extends MP2Test {
	@SuppressWarnings("boxing")
	public void testRangeList0() {
		assertEval("return [0 .. 3]", Arrays.asList(0, 1, 2, 3));
	}

	public void testRangeList1() {
		assertEval("return [0 .. 3][2]", Integer.valueOf(2));
	}

	public void testRangeList2() {
		assertException("return [0 .. 3][4]", IndexOutOfBoundsException.class);
	}

	public void testRangeList3() {
		assertEval("return [0 .. 3] == [ 0, 1, 2, 3]", Boolean.TRUE);
	}

	public void testRangeList4() {
		assertException("return [0 .. ].size()", ParsingException.class);
	}

	public void testRangeList5() {
		// currently the list cap is (Integer.MAX_VALUE - 1) because of overflow issues
		assertEval("return (0 .. ).size()", Integer.valueOf(Integer.MAX_VALUE));
	}

	public void testRangeList6() {
		assertEval("return [ -2 .. 2] == [ -2, -1, 0, 1, 2 ]", Boolean.TRUE);
	}

	public void testRangeList7() {
		final String script = //
		/**/"l = [ -2 .. 2]\n" +
		/**/"i = l.iterator()\n\n" +
		/**/"retval = ''\n" +
		/**/"while (i.hasNext())\n" +
		/**/"	retval += i.next()\n\n" +
		/**/"return retval";
		assertEval(script, "-2-1012");
	}

	public void testRangeList8() {
		assertEval("return ( -2 .. 2 ) == ( -2, -1, 0, 1, 2 )", Boolean.TRUE);
	}
}
