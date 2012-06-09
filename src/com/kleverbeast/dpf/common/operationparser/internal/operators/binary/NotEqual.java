package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;


import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class NotEqual extends BinaryOperatorExpression {
	private final Equal mEqual;

	public NotEqual(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
		mEqual = new Equal(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return (mEqual.execute(aScope) == Boolean.TRUE) ? Boolean.FALSE : Boolean.TRUE;
	}
}
