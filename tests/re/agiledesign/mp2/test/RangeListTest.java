package re.agiledesign.mp2.test;

import java.util.Arrays;

import org.junit.Test;

import re.agiledesign.mp2.exception.ParsingException;

public class RangeListTest extends MP2Test {
	@SuppressWarnings("boxing")
	@Test
	public void rangeList0() {
		assertEval("return [0 .. 3]", Arrays.asList(0, 1, 2, 3));
	}

	@Test
	public void rangeList1() {
		assertEval("return [0 .. 3][2]", Integer.valueOf(2));
	}

	@Test
	public void rangeList2() {
		assertException("return [0 .. 3][4]", IndexOutOfBoundsException.class);
	}

	@Test
	public void rangeList3() {
		assertEval("return [0 .. 3] == [ 0, 1, 2, 3]", Boolean.TRUE);
	}

	@Test
	public void rangeList4() {
		assertException("return [0 .. ].size()", ParsingException.class);
	}

	@Test
	public void rangeList5() {
		// currently the list cap is (Integer.MAX_VALUE - 1) because of overflow issues
		assertEval("return (0 .. ).size()", Integer.valueOf(Integer.MAX_VALUE));
	}

	@Test
	public void rangeList6() {
		assertEval("return [ -2 .. 2] == [ -2, -1, 0, 1, 2 ]", Boolean.TRUE);
	}

	@Test
	public void rangeList7() {
		final String script = //
		/**/"l = [ -2 .. 2]\n" +
		/**/"i = l.iterator()\n\n" +
		/**/"retval = ''\n" +
		/**/"while (i.hasNext())\n" +
		/**/"	retval += i.next()\n\n" +
		/**/"return retval";
		assertEval(script, "-2-1012");
	}

	@Test
	public void rangeList8() {
		assertEval("return ( -2 .. 2 ) == ( -2, -1, 0, 1, 2 )", Boolean.TRUE);
	}
}
