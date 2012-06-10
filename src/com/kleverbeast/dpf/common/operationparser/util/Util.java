package com.kleverbeast.dpf.common.operationparser.util;

public class Util {
	public static <T> String getClassString(final T aObject) {
		return (aObject != null) ? aObject.getClass().toString() : "(null)";
	}

	public static <T> String arrayJoin(final String aSeparator, final T... aArgs) {
		final StringBuilder retval = new StringBuilder(128);
		arrayJoin(aSeparator != null ? aSeparator : "", aArgs, retval);

		return retval.toString();
	}

	public static void arrayJoin(final String aSeparator, final Object aArray[], final StringBuilder aSb) {
		if ((aArray != null) && (aArray.length != 0)) {
			final Object first = aArray[0];

			if ((first != null) && first.getClass().isArray()) {
				arrayJoin(aSeparator, (Object[]) first, aSb);
			} else {
				aSb.append(first);
			}

			for (int i = 1; i < aArray.length; ++i) {
				final Object element = aArray[i];

				aSb.append(aSeparator);
				if ((element != null) && element.getClass().isArray()) {
					arrayJoin(aSeparator, (Object[]) element, aSb);
				} else {
					aSb.append(element);
				}
			}
		}
	}
}
