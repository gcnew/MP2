package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.binary;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class RefNotEqual extends BinaryOperatorExpression {
	public RefNotEqual(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return (mLeft.execute(aScope) != mRight.execute(aScope)) ? Boolean.TRUE : Boolean.FALSE;
	}
}
