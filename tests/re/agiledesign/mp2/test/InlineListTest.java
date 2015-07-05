package re.agiledesign.mp2.test;

import java.util.Arrays;
import java.util.List;

public class InlineListTest extends MP2Test {
	public void testInlineList0() {
		assertEval("return [1, 2, 3][1]", Integer.valueOf(2));
	}

	public void testInlineList1() {
		assertEval("return [1, [ (a) => return a * 2 ], 3][1][0](5).toString()", "10");
	}

	public void testInlineList2() {
		assertTrue(eval("return []") instanceof List);
	}

	public void testInlineList3() {
		assertEval("return [].isEmpty()", Boolean.TRUE);
	}

	public void testInlineList4() {
		assertEval("return (1 : []).first()", Integer.valueOf(1));
	}

	@SuppressWarnings("boxing")
	public void testInlineList5() {
		assertEval("return (1 : 2 : 3 : null).rest()", Arrays.asList(2, 3));
	}

	public void testInlineEvalOrder() {
		final List<Integer> expected = Arrays.asList(1, 2, 3);

		assertEval("local i = 0; return ( ++i, ++i, ++i )", expected);
		assertEval("local i = 0; return [ ++i, ++i, ++i ]", expected);
		assertEval("local i = 3; return ( i-- : i-- : i-- : null )", expected);
		assertEval("local i = 3; return ( i-- : ( i-- : ( i-- : null)))", expected);
		assertEval("local i = 0; return ( ++i .. i + 2 )", expected);
		assertEval("local i = 0; return [ ++i .. i + 2 ]", expected);
	}
}
