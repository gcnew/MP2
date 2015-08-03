package re.agiledesign.mp2.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import re.agiledesign.mp2.util.StringUtil;

@SuppressWarnings("boxing")
public class StringFormatTest {
	@Test
	public void format1() {
		assertEquals("Hello, World!", StringUtil.format("Hello, {}!", "World"));
	}

	@Test
	public void format2() {
		assertEquals("Hello, 1, 2, 1", StringUtil.format("Hello, {0}, {1}, {}", 1, 2));
	}

	@Test
	public void format3() {
		assertEquals("Hello, 1, 2, null", StringUtil.format("Hello, {}, {}, {4}", 1, 2));
	}

	@Test
	public void format4() {
		assertEquals("Hello, null, null, {-1}", StringUtil.format("Hello, {}, {0}, {-1}", null, null));
	}

	@Test
	public void format5() {
		assertEquals("", StringUtil.format(""));
	}

	@Test(expected = NullPointerException.class)
	public void format6() {
		assertEquals(null, StringUtil.format(null));
	}
}
