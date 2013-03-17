package re.agiledesign.mp2.internal.expressions.operators.binary;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;

public class RefEqual extends BinaryOperatorExpression {
	public RefEqual(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return (mLeft.execute(aScope) == mRight.execute(aScope)) ? Boolean.TRUE : Boolean.FALSE;
	}
}
