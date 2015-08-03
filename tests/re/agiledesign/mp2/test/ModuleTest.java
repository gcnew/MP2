package re.agiledesign.mp2.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import re.agiledesign.mp2.internal.sourceprovider.SourceProvider;
import re.agiledesign.mp2.internal.sourceprovider.StringSourceProvider;

public class ModuleTest extends MP2Test {
	@Test
	public void modules() {
		final SourceProvider provider = new StringSourceProvider(new String[][] {
			{ "a.mp", "load 'b.mp'; return hello()" },
			{ "b.mp", "function hello() { return 'Hello world'; }" } });

		assertEval(provider, "a.mp", "Hello world");
	}

	@Test
	public void fileModules() {
		assertEval(RESOURCE_PROVIDER, "tick.mp", "tick tock tick tock tick");
	}

	public static class LoadCounter {
		private static int mCounter = 0;

		public static int inc() {
			return ++mCounter;
		}

		public static int counter() {
			return mCounter;
		}
	}

	@Test
	public void singleLoad() {
		final SourceProvider provider = new StringSourceProvider(
			new String[][] {
				{ "a.mp", "load 'inc.mp'; load 'b.mp'; return c();" },
				{ "b.mp", "load 'inc.mp'; load 'c.mp';" },
				{ "c.mp", "load 'inc.mp'; function c() { return 'ola'; }" },
				{
					"inc.mp",
					"0.getClass().forName('" + LoadCounter.class.getName() + "').getMethod('inc').invoke(null);" } });

		assertEval(provider, "a.mp", "ola");
		assertEquals(1, LoadCounter.counter());
	}
}
