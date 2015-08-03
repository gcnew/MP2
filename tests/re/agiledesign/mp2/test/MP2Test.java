package re.agiledesign.mp2.test;

import java.util.Map;

import org.junit.Assert;

import re.agiledesign.mp2.Interpreter;
import re.agiledesign.mp2.InterpreterFactory;
import re.agiledesign.mp2.internal.sourceprovider.FSSourceProvider;
import re.agiledesign.mp2.internal.sourceprovider.SourceProvider;
import re.agiledesign.mp2.util.ArrayUtil;
import re.agiledesign.mp2.util.Util;

public abstract class MP2Test {
	protected static final SourceProvider RESOURCE_PROVIDER;

	static {
		final String resourcesDir = MP2Test.class.getClassLoader().getResource("resources").getPath();

		RESOURCE_PROVIDER = new FSSourceProvider(resourcesDir);
	}

	protected static Object eval(final String aScript) {
		return eval(aScript, null);
	}

	protected static Object eval(final String aScript, final Map<String, ?> aArgs) {
		try {
			final Object retval = InterpreterFactory.eval(aScript, aArgs);
			return retval;
		} catch (final Exception aException) {
			throw Util.rethrowUnchecked(aException);
		}
	}

	protected static Object eval(final SourceProvider aProvider, final String aPath) {
		return eval(aProvider, aPath, null);
	}

	protected static Object eval(final SourceProvider aProvider, final String aPath, final Map<String, ?> aArgs) {
		try {
			final Interpreter interpreter = InterpreterFactory.getInstance(aProvider, aArgs);
			final Object retval = interpreter.evalProvided(aPath);

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

	protected static void assertEval(final String aScript, final Map<String, ?> aArgs, final Object aExpected) {
		Assert.assertEquals(aExpected, eval(aScript, aArgs));
	}

	protected void assertEval(final SourceProvider aProvider, final String aPath, final String aExpected) {
		assertEval(aProvider, aPath, null, aExpected);
	}

	protected void assertEval(final SourceProvider aProvider,
			final String aPath,
			final Map<String, ?> aArgs,
			final String aExpected) {
		Assert.assertEquals(aExpected, eval(aProvider, aPath, aArgs));
	}

	protected static void assertEval(final String aScript,
			final Map<String, ?> aArgs,
			final Object aExpected,
			final EqualsTest aEquals) {
		Assert.assertTrue(aEquals.areEqual(aExpected, eval(aScript, aArgs)));
	}

	protected static void assertException(final String aScript, final Class<? extends Throwable> aExpected) {
		assertException(aScript, null, aExpected);
	}

	protected static void assertException(final String aScript,
			final Map<String, ?> aArgs,
			final Class<? extends Throwable> aExpected) {
		try {
			eval(aScript, aArgs);
		} catch (final Throwable exception) {
			if (!aExpected.isInstance(exception)) {
				throw new IllegalStateException("Expected: " + aExpected.getName() + ", found: " + exception.getClass());
			}

			return;
		}

		Assert.fail();
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
