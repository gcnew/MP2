package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_GREATER_OR_EQ;

import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class IsGreaterOrEqual extends BinaryOperatorExpression {
	public IsGreaterOrEqual(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(IS_GREATER_OR_EQ, left, right);
	}
}
