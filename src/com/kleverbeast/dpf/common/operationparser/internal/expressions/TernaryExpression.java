package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import static com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.isTrue;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class TernaryExpression extends Expression {
	private final Expression mCondition;
	private final Expression mTrue;
	private final Expression mFalse;

	public TernaryExpression(final Expression aCondition, final Expression aTrue, final Expression aFalse) {
		mCondition = aCondition;
		mTrue = aTrue;
		mFalse = aFalse;
	}

	public Object execute(final Scope aScope) throws Exception {
		return (isTrue(mCondition.execute(aScope)) ? mTrue : mFalse).execute(aScope);
	}
}
