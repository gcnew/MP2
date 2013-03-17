package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class LocalAccessExpression extends Expression {
	private final int mIndex;

	public LocalAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		return aScope.getLocalVariable(mIndex);
	}
}
