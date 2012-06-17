package com.kleverbeast.dpf.common.operationparser.internal;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.ADD;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.kleverbeast.dpf.common.operationparser.exception.UnsupportedOperatorException;
import com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType;
import com.kleverbeast.dpf.common.operationparser.util.Util;

public class CoercionUtil {
	public enum CoercionType {
		NULL, OBJECT, BOOLEAN, BYTE, SHORT, CHAR, INT, LONG, BIG_INTEGER, FLOAT, DOUBLE, BIG_DECIMAL, STRING
	}

	private static final Map<Class<?>, Class<?>> mWrapperTypes = new HashMap<Class<?>, Class<?>>();
	private static final Map<Class<?>, CoercionType> mTypesMap = new HashMap<Class<?>, CoercionUtil.CoercionType>();

	static {
		mTypesMap.put(Boolean.class, CoercionType.BOOLEAN);
		mTypesMap.put(Byte.class, CoercionType.BYTE);
		mTypesMap.put(Short.class, CoercionType.SHORT);
		mTypesMap.put(Character.class, CoercionType.CHAR);
		mTypesMap.put(Integer.class, CoercionType.INT);
		mTypesMap.put(Long.class, CoercionType.LONG);
		mTypesMap.put(BigInteger.class, CoercionType.BIG_INTEGER);
		mTypesMap.put(Float.class, CoercionType.FLOAT);
		mTypesMap.put(Double.class, CoercionType.DOUBLE);
		mTypesMap.put(BigDecimal.class, CoercionType.BIG_DECIMAL);
		mTypesMap.put(String.class, CoercionType.STRING);
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
			final Number number = (Number) aObject;

			switch (aType) {
			case BOOLEAN:
				return Boolean.valueOf(number.intValue() != 0);
			case BYTE:
				return Byte.valueOf(number.byteValue());
			case SHORT:
				return Short.valueOf(number.shortValue());
			case CHAR:
				return Character.valueOf((char) number.intValue());
			case INT:
				return Integer.valueOf(number.intValue());
			case LONG:
				return Long.valueOf(number.longValue());
			case BIG_INTEGER:
				if (aObject instanceof BigDecimal) {
					return ((BigDecimal) aObject).toBigInteger();
				}

				if (aObject instanceof BigInteger) {
					return aObject;
				}

				return BigInteger.valueOf(number.longValue());
			case FLOAT:
				return Float.valueOf(number.floatValue());
			case DOUBLE:
				return Double.valueOf(number.doubleValue());
			case BIG_DECIMAL:
				if (aObject instanceof BigInteger) {
					return new BigDecimal((BigInteger) aObject);
				}

				if (aObject instanceof BigDecimal) {
					return aObject;
				}

				return BigDecimal.valueOf(number.doubleValue());
			}
		}

		if (aType == CoercionType.BOOLEAN) {
			return (aObject instanceof Boolean) ? ((Boolean) aObject) : Boolean.valueOf(aObject != null);
		}

		if (aType == CoercionType.STRING) {
			return String.valueOf(aObject);
		}

		throw new IllegalArgumentException("Unexpected type: " + Util.getClassString(aObject));
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
			return aLeft.intValue() < aRight.intValue();
		case IS_LESS_OR_EQ:
			return aLeft.intValue() <= aRight.intValue();
		case IS_GREATER:
			return aLeft.intValue() > aRight.intValue();
		case IS_GREATER_OR_EQ:
			return aLeft.intValue() >= aRight.intValue();
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
			return aLeft.intValue() < aRight.intValue();
		case IS_LESS_OR_EQ:
			return aLeft.intValue() <= aRight.intValue();
		case IS_GREATER:
			return aLeft.intValue() > aRight.intValue();
		case IS_GREATER_OR_EQ:
			return aLeft.intValue() >= aRight.intValue();
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Integer aLeft, final Integer aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Integer.valueOf(aLeft + aRight);
		case SUBSTRACT:
			return Integer.valueOf(aLeft - aRight);
		case MULTIPLY:
			return Integer.valueOf(aLeft * aRight);
		case DIVIDE:
			return Integer.valueOf(aLeft / aRight);
		case MODULO:
			return Integer.valueOf(aLeft % aRight);
		case SHIFT_LEFT:
			return Integer.valueOf(aLeft << aRight);
		case SHIFT_RIGHT:
			return Integer.valueOf(aLeft >> aRight);
		case IS_LESS:
			return aLeft < aRight;
		case IS_LESS_OR_EQ:
			return aLeft <= aRight;
		case IS_GREATER:
			return aLeft > aRight;
		case IS_GREATER_OR_EQ:
			return aLeft >= aRight;
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Long aLeft, final Long aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Long.valueOf(aLeft + aRight);
		case SUBSTRACT:
			return Long.valueOf(aLeft - aRight);
		case MULTIPLY:
			return Long.valueOf(aLeft * aRight);
		case DIVIDE:
			return Long.valueOf(aLeft / aRight);
		case MODULO:
			return Long.valueOf(aLeft % aRight);
		case SHIFT_LEFT:
			return Long.valueOf(aLeft << aRight);
		case SHIFT_RIGHT:
			return Long.valueOf(aLeft >> aRight);
		case IS_LESS:
			return aLeft < aRight;
		case IS_LESS_OR_EQ:
			return aLeft <= aRight;
		case IS_GREATER:
			return aLeft > aRight;
		case IS_GREATER_OR_EQ:
			return aLeft >= aRight;
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
			return aLeft.compareTo(aRight) < 0;
		case IS_LESS_OR_EQ:
			return aLeft.compareTo(aRight) <= 0;
		case IS_GREATER:
			return aLeft.compareTo(aRight) > 0;
		case IS_GREATER_OR_EQ:
			return aLeft.compareTo(aRight) >= 0;
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Float aLeft, final Float aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Float.valueOf(aLeft + aRight);
		case SUBSTRACT:
			return Float.valueOf(aLeft - aRight);
		case MULTIPLY:
			return Float.valueOf(aLeft * aRight);
		case DIVIDE:
			return Float.valueOf(aLeft / aRight);
		case IS_LESS:
			return aLeft < aRight;
		case IS_LESS_OR_EQ:
			return aLeft <= aRight;
		case IS_GREATER:
			return aLeft > aRight;
		case IS_GREATER_OR_EQ:
			return aLeft >= aRight;
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}

	private static Object doOperation(final Double aLeft, final Double aRight, final OperatorType aOp)
			throws UnsupportedOperatorException {
		switch (aOp) {
		case ADD:
			return Double.valueOf(aLeft + aRight);
		case SUBSTRACT:
			return Double.valueOf(aLeft - aRight);
		case MULTIPLY:
			return Double.valueOf(aLeft * aRight);
		case DIVIDE:
			return Double.valueOf(aLeft / aRight);
		case IS_LESS:
			return aLeft < aRight;
		case IS_LESS_OR_EQ:
			return aLeft <= aRight;
		case IS_GREATER:
			return aLeft > aRight;
		case IS_GREATER_OR_EQ:
			return aLeft >= aRight;
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
			return aLeft.compareTo(aRight) < 0;
		case IS_LESS_OR_EQ:
			return aLeft.compareTo(aRight) <= 0;
		case IS_GREATER:
			return aLeft.compareTo(aRight) > 0;
		case IS_GREATER_OR_EQ:
			return aLeft.compareTo(aRight) >= 0;
		}

		throw new UnsupportedOperatorException(aOp, aLeft, aRight);
	}
}
