package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SUBSTRACT;

import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class Substract extends BinaryOperatorExpression {
	public Substract(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(SUBSTRACT, left, right);
	}
}
