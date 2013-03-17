package re.agiledesign.mp2.internal.statements;

import re.agiledesign.mp2.internal.ControlFlow;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;

public class ReturnStatement extends Statement {
	private final Expression mReturnExpression;

	public ReturnStatement(final Expression aReturnExpression) {
		mReturnExpression = aReturnExpression;
	}

	public void execute(final Scope aScope) throws Exception {
		aScope.setControlFlow(ControlFlow.getReturn(mReturnExpression.execute(aScope)));
	}
}
