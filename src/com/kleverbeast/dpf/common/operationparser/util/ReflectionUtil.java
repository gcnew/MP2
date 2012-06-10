package com.kleverbeast.dpf.common.operationparser.util;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.getWrapperClass;

import java.lang.reflect.Method;

public class ReflectionUtil {

	public static Object invokeMethod(final Object aThis, final String aInternedName, final Object aArgs[])
			throws Exception {
		final Class<?> _class = aThis.getClass();
		final Method methods[] = _class.getMethods();

		final Class<?> types[] = new Class<?>[aArgs.length];
		for (int i = 0; i < aArgs.length; ++i) {
			types[i] = (aArgs[i] != null) ? aArgs[i].getClass() : null;
		}

		final Method exactMethod = getExactMethod(aInternedName, methods, types);

		if (exactMethod != null) {
			return exactMethod.invoke(aThis, aArgs);
		}

		/*Node<Method> mAmbiguous = null;
		Node<Method> varArgsMethods = null;
		for (final Method m : methods) {
			if (m.getName() == aInternedName) {
				final Class<?> methodArgs[] = m.getParameterTypes();

				if (aArgTypes.length != methodArgs.length) {
					varArgsMethods = new Node<Method>(m, varArgsMethods);
				}

				int i;
				for (i = 0; i < aArgTypes.length; i++) {
					final Class<?> normClass = getNormalizedClass(methodArgs[i]);

					if (normClass != aArgTypes[i]) {
						final CoercionType type1 = CoercionUtil.getCoercionType(_class);
						final CoercionType type2 = CoercionUtil.getCoercionType(normClass);

						if ((type1.ordinal() < BYTE.ordinal()) || (type2.ordinal() < BYTE.ordinal()))

							break;
					}
				}

				if (i == aArgTypes.length) {
					mAmbiguous = new Node<Method>(m, mAmbiguous);
				}
			}
		}

		if (mAmbiguous != null) {
			if (mAmbiguous.hasNext()) {
				final String signatures = getSignatures(_class, mAmbiguous);
				throw new AmbiguousException("Multiple methods satisfying signutere found", signatures);
			} else {
				return mAmbiguous.getValue();
			}
		}*/

		throw new NoSuchMethodException("Method " + getSignature(_class, aInternedName, types) + " not found");
	}

	private static Method getExactMethod(final String aInternedName, final Method[] aMethods, final Class<?> aTypes[]) {
		for (final Method m : aMethods) {
			if (m.getName() == aInternedName) {
				final Class<?> methodArgs[] = m.getParameterTypes();

				if (aTypes.length != methodArgs.length) {
					continue;
				}

				int i;
				for (i = 0; i < aTypes.length; i++) {
					if (methodArgs[i].isPrimitive()) {
						if ((aTypes[i] == null) || (aTypes[i] != getWrapperClass(methodArgs[i]))) {
							break;
						}
					} else {
						if ((aTypes[i] != null) && (aTypes[i] != methodArgs[i])) {
							break;
						}
					}
				}

				if (i == methodArgs.length) {
					return m;
				}
			}
		}

		return null;
	}

	private static Class<?> getNormalizedClass(final Class<?> aClass) {
		return (aClass.isPrimitive()) ? getWrapperClass(aClass) : aClass;
	}

	private static String getSignature(final Class<?> aClass, final String aMethod, final Class<?> aArgTypes[]) {
		final StringBuilder sb = new StringBuilder(128);

		final String argTypes[] = new String[aArgTypes.length];
		for (int i = 0; i < aArgTypes.length; i++) {
			argTypes[i] = (aArgTypes[i] != null) ? aArgTypes[i].getName() : "null";
		}

		sb.append(aClass.getName()).append('.').append(aMethod).append('(');
		Util.arrayJoin(", ", argTypes, sb);
		sb.append(')');

		return sb.toString();
	}

	private static String getSignatures(final Class<?> aClass, final Node<Method> aMethods) {
		final StringBuilder retval = new StringBuilder(256);

		boolean first = true;
		for (final Method m : aMethods) {
			if (!first) {
				retval.append("\n");
			}

			retval.append(getSignature(aClass, m.getName(), m.getParameterTypes()));
		}

		return retval.toString();
	}
}
