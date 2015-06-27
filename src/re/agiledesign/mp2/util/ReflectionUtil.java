package re.agiledesign.mp2.util;

import static re.agiledesign.mp2.util.CoercionUtil.getWrapperClass;

import java.lang.reflect.Method;
import java.util.List;

import re.agiledesign.mp2.collection.ConsList;
import re.agiledesign.mp2.collection.SequentialList;
import re.agiledesign.mp2.exception.AmbiguousException;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.CastExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.util.CoercionUtil.CoercionType;

public class ReflectionUtil {

	public static Object invokeMethod(final Object aThis,
			final String aInternedName,
			final Scope aScope,
			final List<Expression> aArgs) throws Exception {
		int i = 0;
		boolean noNull = true;
		final Class<?> types[] = new Class<?>[aArgs.size()];
		final Object args[] = new Object[aArgs.size()];

		for (final Expression e : aArgs) {
			final Object value = e.execute(aScope);

			if (value != null) {
				types[i] = value.getClass();
			} else {
				if (e instanceof CastExpression) {
					types[i] = ((CastExpression) e).getType().getJavaType();
				} else {
					types[i] = null;
					noNull = false;
				}
			}

			args[i++] = value;
		}

		return invokeMethod(aThis, aInternedName, args, types, noNull);
	}

	public static Object invokeMethod(final Object aThis,
			final String aInternedName,
			final Object aArgs[],
			final Class<?> aTypes[],
			final boolean aNoNull) throws Exception {
		final Class<?> _class = aThis.getClass();
		final Method methods[] = _class.getMethods();

		if (aNoNull) {
			final Method exactMethod = getExactMethod(aInternedName, methods, aTypes);
			if (exactMethod != null) {
				return safeInvoke(exactMethod, aThis, aArgs);
			}
		}

		final MethodSearchRetval convertedMethod = getConvertedMethod(aInternedName, aArgs, _class, methods, aTypes);
		if (convertedMethod != null) {
			return safeInvoke(convertedMethod.method, aThis, convertedMethod.convertedArgs);
		}

		throw new NoSuchMethodException("Method " + getSignature(_class, aInternedName, aTypes) + " not found");
	}

	public static Object invokeMethod(final Object aThis, final String aInternedName, final Object aArgs[])
			throws Exception {
		boolean noNull = true;
		final Class<?>[] types = new Class[aArgs.length];

		for (int i = 0; i < aArgs.length; ++i) {
			if (aArgs[i] == null) {
				noNull = false;
			} else {
				types[i] = aArgs[i].getClass();
			}
		}

		return invokeMethod(aThis, aInternedName, aArgs, types, noNull);
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
						if (aTypes[i] != getWrapperClass(methodArgs[i])) {
							break;
						}
					} else {
						if (aTypes[i] != methodArgs[i]) {
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

	private static MethodSearchRetval getConvertedMethod(final String aInternedName,
			final Object[] aArgs,
			final Class<?> aClass,
			final Method[] aMethods,
			final Class<?>[] aTypes) throws AmbiguousException {
		SequentialList<Method> ambiguous = null;
		SequentialList<Method> varArgsMethods = null;

		final Object[] convertedArgs = new Object[aArgs.length];
		for (final Method m : aMethods) {
			if (m.getName() == aInternedName) {
				final Class<?> methTypes[] = m.getParameterTypes();

				if (aTypes.length != methTypes.length) {
					if (m.isVarArgs()) {
						varArgsMethods = new ConsList<Method>(m, varArgsMethods);
					}

					continue;
				}

				int i;
				for (i = 0; i < aTypes.length; i++) {
					if (!convertArg(i, aArgs[i], aTypes[i], convertedArgs, methTypes[i])) {
						break;
					}
				}

				if (i == aTypes.length) {
					ambiguous = new ConsList<Method>(m, ambiguous);
				}
			}
		}

		if (ambiguous != null) {
			if (ambiguous.rest() != null) {
				final String signatures = getSignatures(aClass, ambiguous);
				throw new AmbiguousException("Multiple methods satisfying signature found", signatures);
			}

			return new MethodSearchRetval(ambiguous.first(), convertedArgs);
		}

		return (varArgsMethods != null) ? getVarArgMethod(aClass, varArgsMethods, aArgs, aTypes) : null;
	}

	private static boolean convertArg(final int aIndex,
			final Object aArg,
			final Class<?> aType,
			final Object[] aConvertedArgs,
			final Class<?> aMethodArg) {
		final Class<?> methType;

		if (aMethodArg.isPrimitive()) {
			if (aType == null) {
				return false;
			}

			methType = getWrapperClass(aMethodArg);
		} else {
			if (aType == null) {
				aConvertedArgs[aIndex] = null;
				return true;
			}

			methType = aMethodArg;
		}

		if ((methType != aType) && !methType.isAssignableFrom(aType)) {
			final CoercionType type1 = CoercionUtil.getCoercionType(aType);
			final CoercionType type2 = CoercionUtil.getCoercionType(methType);

			if (CoercionUtil.isNumber(type1) && CoercionUtil.isNumber(type2)) {
				aConvertedArgs[aIndex] = CoercionUtil.cast(aArg, type2);
			} else {
				return false;
			}
		} else {
			aConvertedArgs[aIndex] = aArg;
		}

		return true;
	}

	private static MethodSearchRetval getVarArgMethod(final Class<?> aClass,
			final SequentialList<Method> aMethods,
			final Object aArgs[],
			final Class<?> aTypes[]) throws AmbiguousException {
		SequentialList<Method> ambiguous = null;

		Object[] convertedArgs = null;
		for (final Method m : aMethods) {
			final Class<?> methTypes[] = m.getParameterTypes();

			if (aTypes.length < (methTypes.length - 1)) {
				continue;
			}

			int i;
			final int nonVarArgsCount = methTypes.length - 1;
			convertedArgs = new Object[methTypes.length];
			for (i = 0; i < nonVarArgsCount; i++) {
				if (!convertArg(i, aArgs[i], aTypes[i], convertedArgs, methTypes[i])) {
					break;
				}
			}

			if (i == nonVarArgsCount) {
				// TODO: create variable argument object
				ambiguous = new ConsList<Method>(m, ambiguous);
			}

		}

		if (ambiguous != null) {
			if (ambiguous.rest() != null) {
				final String signatures = getSignatures(aClass, ambiguous);
				throw new AmbiguousException("Multiple methods satisfying signature found", signatures);
			}

			return new MethodSearchRetval(ambiguous.first(), convertedArgs);
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
		ArrayUtil.arrayJoin(", ", argTypes, sb);
		sb.append(')');

		return sb.toString();
	}

	private static String getSignatures(final Class<?> aClass, final List<Method> aMethods) {
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

	private static Object safeInvoke(final Method aMethod, final Object aThis, final Object[] aArgs) throws Exception {
		try {
			// some public methods may throw if the declaring class is [package] private
			return aMethod.invoke(aThis, aArgs);
		} catch (final IllegalAccessException e) {
			aMethod.setAccessible(true);
			return aMethod.invoke(aThis, aArgs);
		}
	}
}
