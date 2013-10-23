package re.agiledesign.mp2.util;

import java.util.Arrays;

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

	public static boolean equals(final Object aArray1, final Object aArray2) {
		if (aArray1 == aArray2) {
			return true;
		}

		if ((aArray1 == null) || (aArray2 == null) || (aArray1.getClass() != aArray2.getClass())
				|| !(aArray1.getClass().isArray() && aArray1.getClass().isArray())
				|| (aArray1.getClass().getComponentType() != aArray2.getClass().getComponentType())) {
			return false;
		}

		final Class<?> componentType = aArray1.getClass().getComponentType();
		if (!componentType.isPrimitive()) {
			return Arrays.equals((Object[]) aArray1, (Object[]) aArray2);
		}

		if (componentType == int.class) {
			return Arrays.equals((int[]) aArray1, (int[]) aArray2);
		}

		if (componentType == byte.class) {
			return Arrays.equals((byte[]) aArray1, (byte[]) aArray2);
		}

		if (componentType == long.class) {
			return Arrays.equals((long[]) aArray1, (long[]) aArray2);
		}

		if (componentType == double.class) {
			return Arrays.equals((double[]) aArray1, (double[]) aArray2);
		}

		if (componentType == short.class) {
			return Arrays.equals((short[]) aArray1, (short[]) aArray2);
		}

		if (componentType == char.class) {
			return Arrays.equals((char[]) aArray1, (char[]) aArray2);
		}

		if (componentType == float.class) {
			return Arrays.equals((float[]) aArray1, (float[]) aArray2);
		}

		if (componentType == boolean.class) {
			return Arrays.equals((boolean[]) aArray1, (boolean[]) aArray2);
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
