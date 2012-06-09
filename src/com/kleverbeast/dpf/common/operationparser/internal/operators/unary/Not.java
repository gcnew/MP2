package com.kleverbeast.dpf.common.operationparser.internal.operators.unary;


import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.UnaryOperatorExpression;

public class Not extends UnaryOperatorExpression {
	public Not(final Expression aRight) {
		super(aRight);
	}

	public Object execute(Scope aScope) throws Exception {
		final Object o = mRight.execute(aScope);

		if (o instanceof Boolean) {
			return Boolean.valueOf(!((Boolean) o).booleanValue());
		}

		return o == null;
	}
}