package test;

import java.util.Arrays;
import java.util.List;

public class InlineListTest extends MP2Test {
	public void testInlineList0() throws Exception {
		assertEval("return [1, 2, 3][1]", Integer.valueOf(2));
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

	public void testInlineList4() throws Exception {
		assertEval("return (1, ).first()", Integer.valueOf(1));
	}

	@SuppressWarnings("boxing")
	public void testInlineList5() throws Exception {
		assertEval("return (1, 2, 3).rest()", Arrays.asList(2, 3));
	}
}
