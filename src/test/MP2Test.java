package test;

import java.util.Map;

import junit.framework.TestCase;

import com.kleverbeast.dpf.common.operationparser.Interpreter;
import com.kleverbeast.dpf.common.operationparser.InterpreterFactory;

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
		return interpreter(aScript, aArgs).eval();
	}

	protected static void assertEval(final String aScript, final Object aExpected) throws Exception {
		assertEval(aScript, null, aExpected);
	}

	protected static void assertEval(final String aScript, final Map<String, Object> aArgs, final Object aExpected)
			throws Exception {
		assertEquals(aExpected, eval(aScript, aArgs));
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
			final Interpreter interpreter = interpreter(aScript, aArgs);

			interpreter.eval();
		} catch (final Throwable exception) {
			assertTrue(aExpected.isInstance(exception));
		}
	}
}
