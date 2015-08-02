package re.agiledesign.mp2.test;

import java.util.Map;

import junit.framework.TestCase;
import re.agiledesign.mp2.Interpreter;
import re.agiledesign.mp2.InterpreterFactory;
import re.agiledesign.mp2.internal.sourceprovider.NullProvider;
import re.agiledesign.mp2.util.ArrayUtil;
import re.agiledesign.mp2.util.Util;

public abstract class MP2Test extends TestCase {
	protected static Object eval(final String aScript) {
		return eval(aScript, null);
	}

	protected static Object eval(final String aScript, final Map<String, ? extends Object> aArgs) {
		try {
			final Object retval = InterpreterFactory.eval(aScript, aArgs);
			return retval;
		} catch (final Exception aException) {
			throw Util.rethrowUnchecked(aException);
		}
	}

	protected static void assertEval(final String aScript, final Object aExpected) {
		assertEval(aScript, null, aExpected);
	}

	protected static void assertEval(final String aScript, final Object aExpected, final EqualsTest aEquals) {
		assertEval(aScript, null, aExpected, aEquals);
	}

	protected static void assertEval(final String aScript,
			final Map<String, ? extends Object> aArgs,
			final Object aExpected) {
		assertEquals(aExpected, eval(aScript, aArgs));
	}

	protected static void assertEval(final String aScript,
			final Map<String, ? extends Object> aArgs,
			final Object aExpected,
			final EqualsTest aEquals) {
		assertTrue(aEquals.areEqual(aExpected, eval(aScript, aArgs)));
	}

	protected static void assertGlobal(final String aScript, final Object aExpected) {
		assertGlobal(aScript, null, aExpected);
	}

	protected static void assertGlobal(final String aScript,
			final Map<String, ? extends Object> aArgs,
			final Object aExpected) {
		try {
			final Interpreter interpreter = InterpreterFactory.getInstance(NullProvider.instance(), aArgs);

			interpreter.eval(aScript);
			assertEquals(aExpected, interpreter.getGlobal(aScript));
		} catch (final Exception e) {
			throw Util.rethrowUnchecked(e);
		}
	}

	protected static void assertException(final String aScript, final Class<? extends Throwable> aExpected) {
		assertException(aScript, null, aExpected);
	}

	protected static void assertException(final String aScript,
			final Map<String, ? extends Object> aArgs,
			final Class<? extends Throwable> aExpected) {
		try {
			eval(aScript, aArgs);
		} catch (final Throwable exception) {
			if (!aExpected.isInstance(exception)) {
				throw new IllegalStateException("Expected: " + aExpected.getName() + ", found: " + exception.getClass());
			}

			return;
		}

		fail();
	}

	public static final EqualsTest ArrayEquals = new EqualsTest() {
		public boolean areEqual(Object aFirst, Object aSecond) {
			return ArrayUtil.equals(aFirst, aSecond);
		}
	};

	public interface EqualsTest {
		public boolean areEqual(final Object aFirst, final Object aSecond);
	}
}
