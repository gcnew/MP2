package re.agiledesign.mp2.internal.expressions.operators.binary;

import static re.agiledesign.mp2.lexer.OperatorType.IS_LESS;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.util.CoercionUtil;

public class IsLess extends BinaryOperatorExpression {
	public IsLess(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(IS_LESS, left, right);
	}
}
