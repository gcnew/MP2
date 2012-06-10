package com.kleverbeast.dpf.common.operationparser.internal.expressions;

public abstract class UnaryOperatorExpression extends Expression {
	protected final Expression mRight;

	public UnaryOperatorExpression(final Expression aRight) {
		mRight = aRight;
	}
}
