package com.kleverbeast.dpf.common.operationparser.internal;


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