package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.FunctionScope;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class ArgumentAssignmentExpression extends Expression {
	private final int mIndex;
	private final Expression mExpression;

	public ArgumentAssignmentExpression(final int aIndex, final Expression aExpression) {
		mIndex = aIndex;
		mExpression = aExpression;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		final Object value = mExpression.execute(aScope);
		((FunctionScope) aScope).setArgument(mIndex, value);

		return value;
	}
}
