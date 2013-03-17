package com.kleverbeast.dpf.common.operationparser.internal.statements;

import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class SequenceStatement extends Statement {
	private final List<Expression> mExpressions;

	public SequenceStatement(final List<Expression> aExpressions) {
		mExpressions = aExpressions;
	}

	public void execute(final Scope aScope) throws Exception {
		for (final Expression e : mExpressions) {
			e.execute(aScope);
		}
	}
}
