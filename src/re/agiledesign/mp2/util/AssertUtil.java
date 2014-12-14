package re.agiledesign.mp2.util;


public class AssertUtil {
	public static void runtimeAssert(final boolean aCondition) {
		if (!aCondition) {
			throw new AssertionError();
		}
	}

	public static void runtimeAssert(final boolean aCondition, final String aMessage) {
		if (!aCondition) {
			throw new AssertionError(aMessage);
		}
	}

	public static void runtimeAssert(final boolean aCondition, final String aMessage, final Object... aArgs) {
		if (!aCondition) {
			throw new AssertionError(StringUtil.format(aMessage, aArgs));
		}
	}

	public static void notNull(final Object aObject) {
		if (aObject == null) {
			throw new AssertionError();
		}
	}

	public static void notNull(final String aObject, final String aMessage, final Object... aArgs) {
		if (aObject == null) {
			throw new AssertionError(StringUtil.format(aMessage, aArgs));
		}
	}

	public static RuntimeException never() {
		runtimeAssert(false);
		return null;
	}

	public static RuntimeException never(final String aMessage, final Object... aArgs) {
		runtimeAssert(false, aMessage, aArgs);
		return null;
	}
}
