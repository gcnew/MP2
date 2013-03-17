package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.FunctionScope;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class ArgumentAccessExpression extends Expression {
	private final int mIndex;

	public ArgumentAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		return ((FunctionScope) aScope).getArgument(mIndex);
	}
}
