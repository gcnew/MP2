package re.agiledesign.mp2.util;

public class ArrayUtil {
	public static Object atIndex(final Object aArray, final int aIndex) {
		if ((aArray == null) || !aArray.getClass().isArray()) {
			throw new IllegalStateException("Array expected");
		}

		final Class<?> componentType = aArray.getClass().getComponentType();
		if (!componentType.isPrimitive()) {
			return ((Object[]) aArray)[aIndex];
		}

		if (componentType == int.class) {
			return Integer.valueOf(((int[]) aArray)[aIndex]);
		}

		if (componentType == byte.class) {
			return Byte.valueOf(((byte[]) aArray)[aIndex]);
		}

		if (componentType == long.class) {
			return Long.valueOf(((long[]) aArray)[aIndex]);
		}

		if (componentType == double.class) {
			return Double.valueOf(((double[]) aArray)[aIndex]);
		}

		if (componentType == short.class) {
			return Short.valueOf(((short[]) aArray)[aIndex]);
		}

		if (componentType == char.class) {
			return Character.valueOf(((char[]) aArray)[aIndex]);
		}

		if (componentType == float.class) {
			return Float.valueOf(((float[]) aArray)[aIndex]);
		}

		if (componentType == boolean.class) {
			return Boolean.valueOf(((boolean[]) aArray)[aIndex]);
		}

		throw new IllegalStateException("Unexpected primitive type: " + componentType);
	}

	public static Object setIndex(final Object aArray, final int aIndex, final Object aValue) {
		if ((aArray == null) || !aArray.getClass().isArray()) {
			throw new IllegalStateException("Array expected");
		}

		final Class<?> componentType = aArray.getClass().getComponentType();
		if (!componentType.isPrimitive()) {
			return ((Object[]) aArray)[aIndex] = aValue;
		}

		if (componentType == int.class) {
			return Integer.valueOf(((int[]) aArray)[aIndex] = ((Number) aValue).intValue());
		}

		if (componentType == byte.class) {
			return Byte.valueOf(((byte[]) aArray)[aIndex] = ((Number) aValue).byteValue());
		}

		if (componentType == long.class) {
			return Long.valueOf(((long[]) aArray)[aIndex] = ((Number) aValue).longValue());
		}

		if (componentType == double.class) {
			return Double.valueOf(((double[]) aArray)[aIndex] = ((Number) aValue).doubleValue());
		}

		if (componentType == short.class) {
			return Short.valueOf(((short[]) aArray)[aIndex] = ((Number) aValue).shortValue());
		}

		if (componentType == char.class) {
			return Character.valueOf(((char[]) aArray)[aIndex] = ((Character) aValue).charValue());
		}

		if (componentType == float.class) {
			return Float.valueOf(((float[]) aArray)[aIndex] = ((Number) aValue).floatValue());
		}

		if (componentType == boolean.class) {
			return Boolean.valueOf(((boolean[]) aArray)[aIndex] = ((Boolean) aValue).booleanValue());
		}

		throw new IllegalStateException("Unexpected primitive type: " + componentType);
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
