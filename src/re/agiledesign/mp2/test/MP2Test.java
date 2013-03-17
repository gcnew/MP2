package test;

import java.util.Map;

import re.agiledesign.mp2.Interpreter;
import re.agiledesign.mp2.InterpreterFactory;

import junit.framework.TestCase;


public abstract class MP2Test extends TestCase {
	protected static Interpreter interpreter(final String aScript) throws Exception {
		return interpreter(aScript, null);
	}

	protected static Interpreter interpreter(final String aScript, final Map<String, Object> aArgs) throws Exception {
		return InterpreterFactory.createInterpreter(aScript, aArgs);
	}

	protected static Object eval(final String aScript) throws Exception {
		return eval(aScript, null);
	}

	protected static Object eval(final String aScript, final Map<String, Object> aArgs) throws Exception {
		final Object retval = interpreter(aScript, aArgs).eval();
		return retval;
	}

	protected static void assertEval(final String aScript, final Object aExpected) throws Exception {
		assertEval(aScript, null, aExpected);
	}

	protected static void assertEval(final String aScript, final Object aExpected, final EqualsTest aEquals)
			throws Exception {
		assertEval(aScript, null, aExpected, aEquals);
	}

	protected static void assertEval(final String aScript, final Map<String, Object> aArgs, final Object aExpected)
			throws Exception {
		assertEquals(aExpected, eval(aScript, aArgs));
	}

	protected static void assertEval(final String aScript,
			final Map<String, Object> aArgs,
			final Object aExpected,
			final EqualsTest aEquals) throws Exception {
		assertTrue(aEquals.areEqual(aExpected, eval(aScript, aArgs)));
	}

	protected static void assertGlobal(final String aScript, final Object aExpected) throws Exception {
		assertGlobal(aScript, null, aExpected);
	}

	protected static void assertGlobal(final String aScript, final Map<String, Object> aArgs, final Object aExpected)
			throws Exception {
		final Interpreter interpreter = interpreter(aScript, aArgs);

		interpreter.eval();
		assertEquals(aExpected, interpreter.getGlobal(aScript));
	}

	protected static void assertException(final String aScript, final Class<? extends Throwable> aExpected)
			throws Exception {
		assertException(aScript, null, aExpected);
	}

	protected static void assertException(final String aScript,
			final Map<String, Object> aArgs,
			final Class<? extends Throwable> aExpected) throws Exception {
		try {
			eval(aScript, aArgs);
		} catch (final Throwable exception) {
			assertTrue(aExpected.isInstance(exception));

			return;
		}

		fail();
	}

	public interface EqualsTest {
		public boolean areEqual(final Object aFirst, final Object aSecond);
	}
}
