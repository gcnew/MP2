package com.kleverbeast.dpf.common.operationparser;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.getWrapperClass;

import java.lang.reflect.Method;

public class Util {
	public static <T> String getClassString(final T aObject) {
		return (aObject != null) ? aObject.getClass().toString() : "(null)";
	}

	public static Method getMethod(final Object aObject, final String aName, final Class<?> aArgTypes[])
			throws Exception {
		final String name = aName.intern();
		final Method methods[] = aObject.getClass().getMethods();

		for (final Method m : methods) {
			if (m.getName() == name) {
				final Class<?> methodArgs[] = m.getParameterTypes();

				if (aArgTypes.length != methodArgs.length) {
					continue;
				}

				int i;
				for (i = 0; i < aArgTypes.length; i++) {
					if ((aArgTypes[i] != methodArgs[i]) && methodArgs[i].isPrimitive()
							&& (aArgTypes[i] != getWrapperClass(methodArgs[i]))) {
						break;
					}
				}

				if (i == aArgTypes.length) {
					return m;
				}
			}
		}

		throw new NoSuchMethodException("Method " + aName + " not found");
	}
}
