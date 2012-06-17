package com.kleverbeast.dpf.common.operationparser.internal.statements;

import com.kleverbeast.dpf.common.operationparser.internal.ControlFlow;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class ReturnStatement extends Statement {
	private final Expression mReturnExpression;

	public ReturnStatement(final Expression aReturnExpression) {
		mReturnExpression = aReturnExpression;
	}

	public void execute(final Scope aScope) throws Exception {
		aScope.setControlFlow(ControlFlow.getReturn(mReturnExpression.execute(aScope)));
	}
}
