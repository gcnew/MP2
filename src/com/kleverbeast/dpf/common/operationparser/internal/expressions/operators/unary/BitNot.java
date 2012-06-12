package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.unary;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import com.kleverbeast.dpf.common.operationparser.exception.ScriptException;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.UnaryOperatorExpression;

public class BitNot extends UnaryOperatorExpression {
	public BitNot(final Expression aRight) {
		super(aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object o = mRight.execute(aScope);

		if (o instanceof Integer) {
			return Integer.valueOf(~((Integer) o).intValue());
		}

		throw new ScriptException("Operation " + this.getClass().getSimpleName()
				+ " not yet implemented or not applicable for " + getClassString(o));
	}
}