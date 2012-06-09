package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.isTrue;

import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class Or extends BinaryOperatorExpression {
	public Or(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);

		return isTrue(left) ? left : mRight.execute(aScope);
	}
}
