package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.ADD;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.kleverbeast.dpf.common.operationparser.exception.UnsupportedOperatorException;
import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class Add extends BinaryOperatorExpression {
	public Add(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		if (Boolean.TRUE.booleanValue()) {
			return CoercionUtil.doOperation(ADD, left, right);
		}

		if (!(left instanceof Number) || !(right instanceof Number)) {
			if (left instanceof String) {
				return (String) left + right;
			}

			if (right instanceof String) {
				return left + (String) right;
			}

			throw new UnsupportedOperatorException(ADD, left, right);
		}

		if (left instanceof Integer) {
			return coerceInteger((Integer) left, right);
		}

		if (left instanceof Double) {
			return coerceDouble((Double) left, right);
		}

		if (left instanceof Long) {
			return coerceLong((Long) left, right);
		}

		if (left instanceof Float) {
			return coerceFloat((Float) left, right);
		}

		if (left instanceof BigInteger) {
			return coerceBigInteger((BigInteger) left, right);
		}

		if (left instanceof BigDecimal) {
			return coerceBigDecimal((BigDecimal) left, right);
		}

		throw new UnsupportedOperatorException(ADD, left, right);
	}

	private Number coerceInteger(final Integer aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return Integer.valueOf(aLeft + ((Integer) aRight).intValue());
		}

		if (aRight instanceof Double) {
			return Double.valueOf(aLeft.doubleValue() + ((Double) aRight).doubleValue());
		}

		if (aRight instanceof Long) {
			return Long.valueOf(aLeft.longValue() + ((Long) aRight).longValue());
		}

		if (aRight instanceof Float) {
			return Float.valueOf(aLeft.floatValue() + ((Float) aRight).floatValue());
		}

		if (aRight instanceof BigInteger) {
			return ((BigInteger) aRight).add(BigInteger.valueOf(aLeft.longValue()));
		}

		if (aRight instanceof BigDecimal) {
			return ((BigDecimal) aRight).add(BigDecimal.valueOf(aLeft.longValue()));
		}

		if (aRight instanceof Byte) {
			return Integer.valueOf(aLeft + ((Byte) aRight).intValue());
		}

		if (aRight instanceof Short) {
			return Integer.valueOf(aLeft + ((Short) aRight).intValue());
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}

	private Number coerceDouble(final Double aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return Double.valueOf(aLeft + ((Integer) aRight).doubleValue());
		}

		if (aRight instanceof Double) {
			return Double.valueOf(aLeft + ((Double) aRight).doubleValue());
		}

		if (aRight instanceof Long) {
			return Double.valueOf(aLeft + ((Long) aRight).doubleValue());
		}

		if (aRight instanceof Float) {
			return Double.valueOf(aLeft + ((Float) aRight).doubleValue());
		}

		if (aRight instanceof BigInteger) {
			return Double.valueOf(aLeft + ((BigInteger) aRight).doubleValue());
		}

		if (aRight instanceof BigDecimal) {
			return ((BigDecimal) aRight).add(BigDecimal.valueOf(aLeft));
		}

		if (aRight instanceof Byte) {
			return Double.valueOf(aLeft + ((Byte) aRight).doubleValue());
		}

		if (aRight instanceof Short) {
			return Double.valueOf(aLeft + ((Short) aRight).doubleValue());
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}

	private Number coerceLong(final Long aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return Long.valueOf(aLeft + ((Integer) aRight).longValue());
		}

		if (aRight instanceof Double) {
			return Double.valueOf(aLeft.doubleValue() + ((Double) aRight).doubleValue());
		}

		if (aRight instanceof Long) {
			return Long.valueOf(aLeft + ((Long) aRight).longValue());
		}

		if (aRight instanceof Float) {
			return Float.valueOf(aLeft.floatValue() + ((Float) aRight).floatValue());
		}

		if (aRight instanceof BigInteger) {
			return ((BigInteger) aRight).add(BigInteger.valueOf(aLeft));
		}

		if (aRight instanceof BigDecimal) {
			return ((BigDecimal) aRight).add(BigDecimal.valueOf(aLeft));
		}

		if (aRight instanceof Byte) {
			return Long.valueOf(aLeft + ((Byte) aRight).longValue());
		}

		if (aRight instanceof Short) {
			return Long.valueOf(aLeft + ((Short) aRight).longValue());
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}

	private Number coerceFloat(final Float aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return Float.valueOf(aLeft + ((Integer) aRight).floatValue());
		}

		if (aRight instanceof Double) {
			return Double.valueOf(aLeft.doubleValue() + ((Double) aRight).doubleValue());
		}

		if (aRight instanceof Long) {
			return Float.valueOf(aLeft + ((Long) aRight).floatValue());
		}

		if (aRight instanceof Float) {
			return Float.valueOf(aLeft + ((Float) aRight).floatValue());
		}

		if (aRight instanceof BigInteger) {
			return Float.valueOf(aLeft + ((BigInteger) aRight).floatValue());
		}

		if (aRight instanceof BigDecimal) {
			return ((BigDecimal) aRight).add(BigDecimal.valueOf(aLeft.doubleValue()));
		}

		if (aRight instanceof Byte) {
			return Float.valueOf(aLeft + ((Byte) aRight).floatValue());
		}

		if (aRight instanceof Short) {
			return Float.valueOf(aLeft + ((Short) aRight).floatValue());
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}

	private Number coerceBigInteger(final BigInteger aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return aLeft.add(BigInteger.valueOf(((Integer) aRight).longValue()));
		}

		if (aRight instanceof Double) {
			return Double.valueOf(aLeft.doubleValue() + ((Double) aRight).doubleValue());
		}

		if (aRight instanceof Long) {
			return aLeft.add(BigInteger.valueOf(((Long) aRight).longValue()));
		}

		if (aRight instanceof Float) {
			return Float.valueOf(aLeft.floatValue() + ((Float) aRight).floatValue());
		}

		if (aRight instanceof BigInteger) {
			return aLeft.add((BigInteger) aRight);
		}

		if (aRight instanceof BigDecimal) {
			return ((BigDecimal) aRight).add(new BigDecimal(aLeft));
		}

		if (aRight instanceof Byte) {
			return aLeft.add(BigInteger.valueOf(((Byte) aRight).longValue()));
		}

		if (aRight instanceof Short) {
			return aLeft.add(BigInteger.valueOf(((Short) aRight).longValue()));
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}

	private Number coerceBigDecimal(final BigDecimal aLeft, final Object aRight) throws UnsupportedOperatorException {
		if (aRight instanceof Integer) {
			return aLeft.add(BigDecimal.valueOf(((Integer) aRight).longValue()));
		}

		if (aRight instanceof Double) {
			return aLeft.add(BigDecimal.valueOf(((Double) aRight).doubleValue()));
		}

		if (aRight instanceof Long) {
			return aLeft.add(BigDecimal.valueOf(((Long) aRight).longValue()));
		}

		if (aRight instanceof Float) {
			return aLeft.add(BigDecimal.valueOf(((Float) aRight).doubleValue()));
		}

		if (aRight instanceof BigInteger) {
			return aLeft.add(new BigDecimal((BigInteger) aRight));
		}

		if (aRight instanceof BigDecimal) {
			return aLeft.add((BigDecimal) aRight);
		}

		if (aRight instanceof Byte) {
			return aLeft.add(BigDecimal.valueOf(((Byte) aRight).longValue()));
		}

		if (aRight instanceof Short) {
			return aLeft.add(BigDecimal.valueOf(((Short) aRight).longValue()));
		}

		throw new UnsupportedOperatorException(ADD, aLeft, aRight);
	}
}