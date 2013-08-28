package re.agiledesign.mp2.util;

import java.util.Collections;
import java.util.List;

import re.agiledesign.mp2.collection.ImmutableArrayList;

public class Util {
	public static <T> String getClassString(final T aObject) {
		return (aObject != null) ? aObject.getClass().toString() : "(null)";
	}

	public static boolean isLegacyVariable(final String aVarName) {
		return aVarName.charAt(0) == '$';
	}

	public static String stripVariableName(final String aVarName) {
		if (isLegacyVariable(aVarName)) {
			return aVarName.substring(1);
		}

		return aVarName;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Exception> T rethrow(final Exception aException) throws T {
		throw (T) aException;
	}

	public static RuntimeException rethrowUnchecked(final Exception aException) {
		Util.<RuntimeException> rethrow(aException);

		throw new AssertionError("### Error: this code should be unreachable ###");
	}

	public static <T> List<T> immutableList(final List<T> aList) {
		if (aList.isEmpty()) {
			return Collections.emptyList();
		}

		return new ImmutableArrayList<T>(aList);
	}
}
