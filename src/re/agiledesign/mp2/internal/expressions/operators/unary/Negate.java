package re.agiledesign.mp2.internal.expressions.operators.unary;

import static re.agiledesign.mp2.util.Util.getClassString;
import re.agiledesign.mp2.exception.ScriptException;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.UnaryOperatorExpression;

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
