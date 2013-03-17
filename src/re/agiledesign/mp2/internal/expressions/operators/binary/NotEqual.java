package re.agiledesign.mp2.internal.expressions.operators.binary;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;

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
