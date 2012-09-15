package test;

import java.util.List;

public class InlineListTest extends MP2Test {
	public void testInlineList0() throws Exception {
		assertEval("return [1, 2, 3][1]", 2);
	}

	public void testInlineList1() throws Exception {
		assertEval("return [1, [ (a) => return a * 2 ], 3][1][0](5).toString()", "10");
	}

	public void testInlineList2() throws Exception {
		assertTrue(eval("return []") instanceof List);
	}

	public void testInlineList3() throws Exception {
		assertEval("return [].isEmpty()", Boolean.TRUE);
	}
}
