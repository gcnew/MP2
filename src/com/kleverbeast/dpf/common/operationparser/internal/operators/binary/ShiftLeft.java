package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SHIFT_LEFT;


import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class ShiftLeft extends BinaryOperatorExpression {
	public ShiftLeft(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(SHIFT_LEFT, left, right);
	}
}