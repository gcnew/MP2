package com.kleverbeast.dpf.common.operationparser.internal.statements;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class ExpressionStatement extends Statement {
	private final Expression mExpression;

	public ExpressionStatement(final Expression aExpression) {
		mExpression = aExpression;
	}

	public void execute(final Scope aScope) throws Exception {
		mExpression.execute(aScope);
	}
}
