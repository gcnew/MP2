package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Token;

public class ConstantExpression extends Expression {
	private final Object mConstant;

	public ConstantExpression(final Object aObject) {
		mConstant = aObject;
	}

	public ConstantExpression(final Token aToken) {
		mConstant = aToken.getValue();
	}

	public Object execute(Scope aScope) {
		return mConstant;
	}
}
