package re.agiledesign.mp2.internal.statements;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;

public class ExpressionStatement extends Statement {
	private final Expression mExpression;

	public ExpressionStatement(final Expression aExpression) {
		mExpression = aExpression;
	}

	public void execute(final Scope aScope) throws Exception {
		mExpression.execute(aScope);
	}

	public Expression getExpression() {
		return mExpression;
	}
}
