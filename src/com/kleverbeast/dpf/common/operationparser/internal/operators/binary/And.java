package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.isTrue;


import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class And extends BinaryOperatorExpression {
	public And(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);

		return isTrue(left) ? mRight.execute(aScope) : left;
	}
}
