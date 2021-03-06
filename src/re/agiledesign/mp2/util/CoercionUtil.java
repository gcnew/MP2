package re.agiledesign.mp2.util;

import static re.agiledesign.mp2.lexer.OperatorType.ADD;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.exception.UnsupportedOperatorException;
import re.agiledesign.mp2.lexer.OperatorType;

public class CoercionUtil {
	public enum CoercionType {
		NULL(null), //
		OBJECT(null), //
		BOOLEAN(Boolean.class), //
		BYTE(Byte.class), //
		SHORT(Short.class), //
		CHAR(Character.class), //
		INT(Integer.class), //
		LONG(Long.class), //
		BIG_INTEGER(BigInteger.class), //
		FLOAT(Float.class), //
		DOUBLE(Double.class), //
		BIG_DECIMAL(BigDecimal.class), //
		STRING(String.class);

		private final Class<?> mJavaType;

		private CoercionType(final Class<?> aJavaType) {
			mJavaType = aJavaType;
		}

		public Class<?> getJavaType() {
			if (mJavaType == null) {
				throw new IllegalStateException("No direct Java type for: " + this);
			}

			return mJavaType;
		}
	}

	private static final Map<Class<?>, Class<?>> mWrapperTypes = new HashMap<Class<?>, Class<?>>();
	private static final Map<Class<?>, CoercionType> mTypesMap = new HashMap<Class<?>, CoercionUtil.CoercionType>();

	static {
		for (final CoercionType type : CoercionType.values()) {
			// use the type field directly
			if (type.mJavaType != null) {
				mTypesMap.put(type.mJavaType, type);
			}
		}
	}

	static {
		mWrapperTypes.put(boolean.class, Boolean.class);
		mWrapperTypes.put(byte.class, Byte.class);
		mWrapperTypes.put(short.class, Short.class);
		mWrapperTypes.put(char.class, Character.class);
		mWrapperTypes.put(int.class, Integer.class);
		mWrapperTypes.put(long.class, Long.class);
		mWrapperTypes.put(float.class, Float.class);
		mWrapperTypes.put(double.class, Double.class);
	}

	public static CoercionType getCoercionType(final Class<?> aClass) {
		final CoercionType retval = mTypesMap.get(aClass);

		return (retval == null) ? CoercionType.OBJECT : retval;
	}

	public static CoercionType getCoercionType(final Object aObject) {
		if (aObject == null) {
			return CoercionType.NULL;
		}

		return getCoercionType(aObject.getClass());
	}

	public static Class<?> getWrapperClass(final Class<?> aType) {
		final Class<?> retval = mWrapperTypes.get(aType);

		if (retval == null) {
			throw new IllegalArgumentException("Not a primitive type: " + aType);
		}

		return retval;
	}

	public static Object cast(final Object aObject, final CoercionType aType) {
		if (aObject instanceof Number) {
			return castNumber((Number) aObject, aType);
		}

		// parse from string
		if (aObject instanceof String) {
			return parseFromString((String) aObject, aType);
		}

		if (aType == CoercionType.BOOLEAN) {
			return (aObject instanceof Boolean) ? ((Boolean) aObject) : Boolean.valueOf(aObject != null);
		}

		if (aType == CoercionType.STRING) {
			return (aObject == null) ? null : String.valueOf(aObject);
		}

		if (aObject == null) {
			return null;
		}

		throw new IllegalArgumentException("Unexpected type: " + Util.getClassString(aObject));
	}

	private static Object castNumber(final Number aNumber, final CoercionType aType) {
		switch (aType) {
		case BOOLEAN:
			return Boolean.valueOf(aNumber.intValue() != 0);
		case BYTE:
			return Byte.valueOf(aNumber.byteValue());
		case SHORT:
			return Short.valueOf(aNumber.shortValue());
		case CHAR:
			return Character.valueOf((char) aNumber.intValue());
		case INT:
			return Integer.valueOf(aNumber.intValue());
		case LONG:
			return Long.valueOf(aNumber.longValue());
		case BIG_INTEGER:
			if (aNumber instanceof BigDecimal) {
				return ((BigDecimal) aNumber).toBigInteger();
			}

			if (aNumber instanceof BigInteger) {
				return aNumber;
			}

			return BigInteger.valueOf(aNumber.longValue());
		case FLOAT:
			return Float.valueOf(aNumber.floatValue());
		case DOUBLE:
			return Double.valueOf(aNumber.doubleValue());
		case BIG_DECIMAL:
			if (aNumber instanceof BigInteger) {
				return new BigDecimal((BigInteger) aNumber);
			}

			if (aNumber instanceof BigDecimal) {
				return aNumber;
			}

			return BigDecimal.valueOf(aNumber.doubleValue());
		case STRING:
			return aNumber.toString();
		default:
			throw new IllegalStateException("Unexpected type: " + aType);
		}
	}

	private static Object parseFromString(final String aObject, final CoercionType aType) {
		switch (aType) {
		case BOOLEAN:
			return "false".equalsIgnoreCase(aObject) ? Boolean.FALSE : Boolean.TRUE;
		case BYTE:
			return Byte.valueOf(Double.valueOf(aObject).byteValue());
		case SHORT:
			return Short.valueOf(Double.valueOf(aObject).shortValue());
		case CHAR:
			return Character.valueOf((char) Double.valueOf(aObject).intValue());
		case INT:
			return Integer.valueOf(Double.valueOf(aObject).intValue());
		case LONG:
			return Long.valueOf(Double.valueOf(aObject).longValue());
		case BIG_INTEGER:
			return new BigDecimal(aObject).toBigInteger();
		case FLOAT:
			return Float.valueOf(aObject);
		case DOUBLE:
			return Double.valueOf(aObject);
		case BIG_DECIMAL:
			return new BigDecimal(aObject);
		case STRING:
			return aObject;
		default:
			throw new IllegalStateException("Unexpected type: " + aType);
		}
	}

	public static boolean isFloating(final Number aNumber) {
		return (aNumber instanceof Double) || (aNumber instanceof Float);
	}

	public static boolean isIntegral(final Number aNumber) {
		return !isFloating(aNumber);
	}

	public static boolean isNumber(final CoercionType aType) {
		return (aType.ordinal() > CoercionType.BOOLEAN.ordinal()) && (aType.ordinal() < CoercionType.STRING.ordinal());
	}

	public static boolean isTrue(final Object aObject) {
		return ((Boolean) cast(aObject, CoercionType.BOOLEAN)).booleanValue();
	}

	public static Object doOperation(final OperatorType aOperatorType, final Object aLeft, final Object aRight)
			throws UnsupportedOperatorException {
		final CoercionType left0 = getCoercionType(aLeft);
		final CoercionType right0 = getCoercionType(aRight);

		final CoercionType maxType = (left0.ordinal() > right0.ordinal()) ? left0 : right0;

		final Object left;
		final Object right;
		if (left0 != maxType) {
			right = aRight;
			left = cast(aLeft, maxType);
		} else {
			if (right0 != maxType) {
				left = aLeft;
				right = cast(aRight, maxType);
			} else {
				left = aLeft;
				right = aRight;
			}
		}

		switch (maxType) {
		case STRING:
			if (aOperatorType == ADD) {
				return (String) left + (String) right;
			}
			break;
		case BYTE:
			return doOperation((Byte) left, (Byte) right, aOperatorType);
		case SHORT:
			return doOperation((Short) left, (Short) right, aOperatorType);
		case INT:
			return doOperation((Integer) left, (Integer) right, aOperatorType);
		case LONG:
			return doOperation((Long) left, (Long) right, aOperatorType);
		case BIG_INTEGER:
			return doOperation((BigInteger) left, (BigInteger) right, aOperatorType);
		case FLOAT:
			return doOperation((Float) left, (Float) right, aOperatorType);
		case DOUBLE:
			return doOperation((Double) left, (Double) right, aOperatorType);
		case BIG_DECIMAL:
			return doOperation((BigDecimal) left, (BigDecimal) right, aOperatorType);
		}

		throw new UnsupportedOperatorException(aOperatorType, left, aRight);
	}

	private static Object doOperation(final Byte aLeft, final Byte aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Byte.valueOf((byte) (aLeft.intValue() + aRight.intValue()));
		case SUBSTRACT:
			return Byte.valueOf((byte) (aLeft.intValue() - aRight.intValue()));
		case MULTIPLY:
			return Byte.valueOf((byte) (aLeft.intValue() * aRight.intValue()));
		case DIVIDE:
			return Byte.valueOf((byte) (aLeft.intValue() / aRight.intValue()));
		case MODULO:
			return Byte.valueOf((byte) (aLeft.intValue() % aRight.intValue()));
		case SHIFT_LEFT:
			return Byte.valueOf((byte) (aLeft.intValue() << aRight.intValue()));
		case SHIFT_RIGHT:
			return Byte.valueOf((byte) (aLeft.intValue() >> aRight.intValue()));
		case IS_LESS:
			return Boolean.valueOf(aLeft.intValue() < aRight.intValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() <= aRight.intValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.intValue() > aRight.intValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() >= aRight.intValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Short aLeft, final Short aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Short.valueOf((short) (aLeft.intValue() + aRight.intValue()));
		case SUBSTRACT:
			return Short.valueOf((short) (aLeft.intValue() - aRight.intValue()));
		case MULTIPLY:
			return Short.valueOf((short) (aLeft.intValue() * aRight.intValue()));
		case DIVIDE:
			return Short.valueOf((short) (aLeft.intValue() / aRight.intValue()));
		case MODULO:
			return Short.valueOf((short) (aLeft.intValue() % aRight.intValue()));
		case SHIFT_LEFT:
			return Short.valueOf((short) (aLeft.intValue() << aRight.intValue()));
		case SHIFT_RIGHT:
			return Short.valueOf((short) (aLeft.intValue() >> aRight.intValue()));
		case IS_LESS:
			return Boolean.valueOf(aLeft.intValue() < aRight.intValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() <= aRight.intValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.intValue() > aRight.intValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() >= aRight.intValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Integer aLeft, final Integer aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Integer.valueOf(aLeft.intValue() + aRight.intValue());
		case SUBSTRACT:
			return Integer.valueOf(aLeft.intValue() - aRight.intValue());
		case MULTIPLY:
			return Integer.valueOf(aLeft.intValue() * aRight.intValue());
		case DIVIDE:
			return Integer.valueOf(aLeft.intValue() / aRight.intValue());
		case MODULO:
			return Integer.valueOf(aLeft.intValue() % aRight.intValue());
		case SHIFT_LEFT:
			return Integer.valueOf(aLeft.intValue() << aRight.intValue());
		case SHIFT_RIGHT:
			return Integer.valueOf(aLeft.intValue() >> aRight.intValue());
		case IS_LESS:
			return Boolean.valueOf(aLeft.intValue() < aRight.intValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() <= aRight.intValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.intValue() > aRight.intValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.intValue() >= aRight.intValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Long aLeft, final Long aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Long.valueOf(aLeft.longValue() + aRight.longValue());
		case SUBSTRACT:
			return Long.valueOf(aLeft.longValue() - aRight.longValue());
		case MULTIPLY:
			return Long.valueOf(aLeft.longValue() * aRight.longValue());
		case DIVIDE:
			return Long.valueOf(aLeft.longValue() / aRight.longValue());
		case MODULO:
			return Long.valueOf(aLeft.longValue() % aRight.longValue());
		case SHIFT_LEFT:
			return Long.valueOf(aLeft.longValue() << aRight.longValue());
		case SHIFT_RIGHT:
			return Long.valueOf(aLeft.longValue() >> aRight.longValue());
		case IS_LESS:
			return Boolean.valueOf(aLeft.longValue() < aRight.longValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.longValue() <= aRight.longValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.longValue() > aRight.longValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.longValue() >= aRight.longValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final BigInteger aLeft, final BigInteger aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return aLeft.add(aRight);
		case SUBSTRACT:
			return aLeft.subtract(aRight);
		case MULTIPLY:
			return aLeft.multiply(aRight);
		case DIVIDE:
			return aLeft.divide(aRight);
		case MODULO:
			return aLeft.mod(aRight);
		case SHIFT_LEFT:
			return aLeft.shiftLeft(aRight.intValue());
		case SHIFT_RIGHT:
			return aLeft.shiftRight(aRight.intValue());
		case IS_LESS:
			return Boolean.valueOf(aLeft.compareTo(aRight) < 0);
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.compareTo(aRight) <= 0);
		case IS_GREATER:
			return Boolean.valueOf(aLeft.compareTo(aRight) > 0);
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.compareTo(aRight) >= 0);
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Float aLeft, final Float aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Float.valueOf(aLeft.floatValue() + aRight.floatValue());
		case SUBSTRACT:
			return Float.valueOf(aLeft.floatValue() - aRight.floatValue());
		case MULTIPLY:
			return Float.valueOf(aLeft.floatValue() * aRight.floatValue());
		case DIVIDE:
			return Float.valueOf(aLeft.floatValue() / aRight.floatValue());
		case IS_LESS:
			return Boolean.valueOf(aLeft.floatValue() < aRight.floatValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.floatValue() <= aRight.floatValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.floatValue() > aRight.floatValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.floatValue() >= aRight.floatValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Double aLeft, final Double aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Double.valueOf(aLeft.doubleValue() + aRight.doubleValue());
		case SUBSTRACT:
			return Double.valueOf(aLeft.doubleValue() - aRight.doubleValue());
		case MULTIPLY:
			return Double.valueOf(aLeft.doubleValue() * aRight.doubleValue());
		case DIVIDE:
			return Double.valueOf(aLeft.doubleValue() / aRight.doubleValue());
		case IS_LESS:
			return Boolean.valueOf(aLeft.doubleValue() < aRight.doubleValue());
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.doubleValue() <= aRight.doubleValue());
		case IS_GREATER:
			return Boolean.valueOf(aLeft.doubleValue() > aRight.doubleValue());
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.doubleValue() >= aRight.doubleValue());
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final BigDecimal aLeft, final BigDecimal aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return aLeft.add(aRight);
		case SUBSTRACT:
			return aLeft.subtract(aRight);
		case MULTIPLY:
			return aLeft.multiply(aRight);
		case DIVIDE:
			return aLeft.divide(aRight);
		case IS_LESS:
			return Boolean.valueOf(aLeft.compareTo(aRight) < 0);
		case IS_LESS_OR_EQ:
			return Boolean.valueOf(aLeft.compareTo(aRight) <= 0);
		case IS_GREATER:
			return Boolean.valueOf(aLeft.compareTo(aRight) > 0);
		case IS_GREATER_OR_EQ:
			return Boolean.valueOf(aLeft.compareTo(aRight) >= 0);
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}
}
