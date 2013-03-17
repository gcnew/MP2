package re.agiledesign.mp2.internal.expressions.operators.unary;

import static re.agiledesign.mp2.util.CoercionUtil.isTrue;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.UnaryOperatorExpression;


public class Not extends UnaryOperatorExpression {
	public Not(final Expression aRight) {
		super(aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object o = mRight.execute(aScope);

		return isTrue(o) ? Boolean.FALSE : Boolean.TRUE;
	}
}
