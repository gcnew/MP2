package com.kleverbeast.dpf.common.operationparser.internal.expressions.operators.unary;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import com.kleverbeast.dpf.common.operationparser.exception.ScriptException;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.UnaryOperatorExpression;

public class Negate extends UnaryOperatorExpression {
	public Negate(final Expression aRight) {
		super(aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object o = mRight.execute(aScope);

		if (!(o instanceof Number)) {
			throw new ScriptException("Number expected but found " + getClassString(o) + ", " + o);
		}

		if (o instanceof Integer) {
			return Integer.valueOf(-((Integer) o).intValue());
		}

		if (o instanceof Double) {
			return Double.valueOf(-((Double) o).doubleValue());
		}

		throw new ScriptException("Operation " + this.getClass().getSimpleName() + " not yet implemented for "
				+ getClassString(o));
	}
}
