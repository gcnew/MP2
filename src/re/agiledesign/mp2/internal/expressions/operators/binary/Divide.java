package re.agiledesign.mp2.internal.expressions.operators.binary;

import static re.agiledesign.mp2.tokenizer.OperatorType.DIVIDE;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.util.CoercionUtil;

public class Divide extends BinaryOperatorExpression {
	public Divide(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(DIVIDE, left, right);
	}
}