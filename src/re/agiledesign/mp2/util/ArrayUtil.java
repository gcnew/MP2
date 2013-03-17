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
}
