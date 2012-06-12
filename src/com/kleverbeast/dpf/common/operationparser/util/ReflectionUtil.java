package com.kleverbeast.dpf.common.operationparser.util;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.getWrapperClass;

import java.lang.reflect.Method;

import com.kleverbeast.dpf.common.operationparser.exception.AmbiguousException;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.CoercionType;

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

		final MethodSearchRetval convertedMethod = getConvertedMethod(aInternedName, aArgs, _class, methods, types);
		if (convertedMethod.method != null) {
			return convertedMethod.method.invoke(aThis, convertedMethod.convertedArgs);
		}

		throw new NoSuchMethodException("Method " + getSignature(_class, aInternedName, types) + " not found");
	}

	private static MethodSearchRetval getConvertedMethod(final String aInternedName,
			final Object[] aArgs,
			final Class<?> _class,
			final Method[] methods,
			final Class<?>[] types) throws AmbiguousException {
		Node<Method> mAmbiguous = null;
		Node<Method> varArgsMethods = null;

		final Object[] convertedArgs = new Object[aArgs.length];
		for (final Method m : methods) {
			if (m.getName() == aInternedName) {
				final Class<?> methodArgs[] = m.getParameterTypes();

				if (types.length != methodArgs.length) {
					if (m.isVarArgs()) {
						varArgsMethods = new Node<Method>(m, varArgsMethods);
					}

					continue;
				}

				int i;
				for (i = 0; i < types.length; i++) {
					final Class<?> methType;
					final Class<?> type = types[i];

					if (methodArgs[i].isPrimitive()) {
						if (type == null) {
							break;
						}

						methType = getWrapperClass(methodArgs[i]);
					} else {
						if (type == null) {
							convertedArgs[i] = null;
							continue;
						}

						methType = methodArgs[i];
					}

					if ((methType != type) && !methType.isAssignableFrom(type)) {
						final CoercionType type1 = CoercionUtil.getCoercionType(type);
						final CoercionType type2 = CoercionUtil.getCoercionType(methType);

						if (CoercionUtil.isNumber(type1) && CoercionUtil.isNumber(type2)) {
							convertedArgs[i] = CoercionUtil.cast(aArgs[i], type2);
						} else {
							break;
						}
					} else {
						convertedArgs[i] = aArgs[i];
					}
				}

				if (i == types.length) {
					mAmbiguous = new Node<Method>(m, mAmbiguous);
				}
			}
		}

		if (mAmbiguous != null) {
			if (mAmbiguous.hasNext()) {
				final String signatures = getSignatures(_class, mAmbiguous);
				throw new AmbiguousException("Multiple methods satisfying signutere found", signatures);
			} else {
				return new MethodSearchRetval(mAmbiguous.getValue(), convertedArgs);
			}
		}

		return getVarArgMethod(varArgsMethods, aArgs, types);
	}

	private static MethodSearchRetval getVarArgMethod(final Node<Method> aMethods,
			final Object aArgs[],
			final Class<?> aTypes[]) {
		return new MethodSearchRetval(null, null);
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
			if (first) {
				first = false;
			} else {
				retval.append("\n");
			}

			retval.append(getSignature(aClass, m.getName(), m.getParameterTypes()));
		}

		return retval.toString();
	}

	private static class MethodSearchRetval {
		final Method method;
		final Object[] convertedArgs;

		public MethodSearchRetval(final Method aMethod, final Object[] aConvertedArgs) {
			method = aMethod;
			convertedArgs = aConvertedArgs;
		}
	}
}
