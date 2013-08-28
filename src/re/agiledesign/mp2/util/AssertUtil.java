package re.agiledesign.mp2.util;

public class AssertUtil {
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
}
