package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.isFloating;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class Equal extends BinaryOperatorExpression {
	public Equal(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		if (left == right) {
			return Boolean.TRUE;
		}

		if ((left == null) || (right == null)) {
			return Boolean.FALSE;
		}

		if (left.equals(right)) {
			return Boolean.TRUE;
		}

		if ((left instanceof Number) && (right instanceof Number)) {
			final boolean flt = isFloating((Number) left) || isFloating((Number) right);

			if (flt) {
				return Boolean.valueOf(((Number) left).doubleValue() == ((Number) right).doubleValue());
			}

			return Boolean.valueOf(((Number) left).longValue() == ((Number) right).longValue());
		}

		return Boolean.FALSE;
	}
}
