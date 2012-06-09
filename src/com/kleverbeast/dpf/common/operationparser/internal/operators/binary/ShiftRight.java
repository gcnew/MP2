package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SHIFT_RIGHT;

import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class ShiftRight extends BinaryOperatorExpression {
	public ShiftRight(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(SHIFT_RIGHT, left, right);
	}
}
