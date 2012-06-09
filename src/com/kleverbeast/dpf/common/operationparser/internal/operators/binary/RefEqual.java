package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;


import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class RefEqual extends BinaryOperatorExpression {
	public RefEqual(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return (mLeft.execute(aScope) == mRight.execute(aScope)) ? Boolean.TRUE : Boolean.FALSE;
	}
}
