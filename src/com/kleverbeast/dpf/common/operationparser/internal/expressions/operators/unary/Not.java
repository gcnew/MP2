package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.unary;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.UnaryOperatorExpression;

public class Not extends UnaryOperatorExpression {
	public Not(final Expression aRight) {
		super(aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object o = mRight.execute(aScope);

		if (o instanceof Boolean) {
			return Boolean.valueOf(!((Boolean) o).booleanValue());
		}

		return o == null;
	}
}
