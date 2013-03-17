package re.agiledesign.mp2.internal.expressions.operators.unary;

import static re.agiledesign.mp2.util.Util.getClassString;
import re.agiledesign.mp2.exception.ScriptException;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.UnaryOperatorExpression;

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
