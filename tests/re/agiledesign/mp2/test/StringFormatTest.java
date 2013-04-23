package re.agiledesign.mp2.test;

import junit.framework.TestCase;
import re.agiledesign.mp2.util.StringUtil;

@SuppressWarnings("boxing")
public class StringFormatTest extends TestCase {
	public void testFormat1() {
		assertEquals("Hello, World!", StringUtil.format("Hello, {}!", "World"));
	}

	public void testFormat2() {
		assertEquals("Hello, 1, 2, 1", StringUtil.format("Hello, {0}, {1}, {}", 1, 2));
	}

	public void testFormat3() {
		assertEquals("Hello, 1, 2, null", StringUtil.format("Hello, {}, {}, {4}", 1, 2));
	}

	public void testFormat4() {
		assertEquals("Hello, null, null, {-1}", StringUtil.format("Hello, {}, {0}, {-1}", null, null));
	}

	public void testFormat5() {
		assertEquals("", StringUtil.format(""));
	}

	public void testFormat6() {
		try {
			assertEquals(null, StringUtil.format(null));
			fail("NPE expected");
		} catch (final NullPointerException e) {
			// expected result
		}
	}
}
