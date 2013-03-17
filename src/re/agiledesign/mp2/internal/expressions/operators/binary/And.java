package re.agiledesign.mp2.internal.expressions.operators.binary;

import static re.agiledesign.mp2.util.CoercionUtil.isTrue;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;


public class And extends BinaryOperatorExpression {
	public And(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);

		return isTrue(left) ? mRight.execute(aScope) : left;
	}
}
