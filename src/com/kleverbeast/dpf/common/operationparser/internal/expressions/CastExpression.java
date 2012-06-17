package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.util.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.CoercionType;

public class CastExpression extends Expression {
	private final CoercionType mType;
	private final Expression mExpression;

	public CastExpression(final CoercionType aType, final Expression aExpression) {
		mType = aType;
		mExpression = aExpression;
	}

	public Object execute(final Scope aScope) throws Exception {
		return CoercionUtil.cast(mExpression.execute(aScope), mType);
	}
}
