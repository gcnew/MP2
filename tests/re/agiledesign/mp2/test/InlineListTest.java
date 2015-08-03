package re.agiledesign.mp2.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class InlineListTest extends MP2Test {
	@Test
	public void inlineList0() {
		assertEval("return [1, 2, 3][1]", Integer.valueOf(2));
	}

	@Test
	public void inlineList1() {
		assertEval("return [1, [ (a) => return a * 2 ], 3][1][0](5).toString()", "10");
	}

	@Test
	public void inlineList2() {
		assertTrue(eval("return []") instanceof List);
	}

	@Test
	public void inlineList3() {
		assertEval("return [].isEmpty()", Boolean.TRUE);
	}

	@Test
	public void inlineList4() {
		assertEval("return (1 : []).first()", Integer.valueOf(1));
	}

	@Test
	@SuppressWarnings("boxing")
	public void inlineList5() {
		assertEval("return (1 : 2 : 3 : null).rest()", Arrays.asList(2, 3));
	}

	@Test
	public void inlineEvalOrder() {
		final List<Integer> expected = Arrays.asList(1, 2, 3);

		assertEval("local i = 0; return ( ++i, ++i, ++i )", expected);
		assertEval("local i = 0; return [ ++i, ++i, ++i ]", expected);
		assertEval("local i = 3; return ( i-- : i-- : i-- : null )", expected);
		assertEval("local i = 3; return ( i-- : ( i-- : ( i-- : null)))", expected);
		assertEval("local i = 0; return ( ++i .. i + 2 )", expected);
		assertEval("local i = 0; return [ ++i .. i + 2 ]", expected);
	}
}
