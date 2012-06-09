package com.kleverbeast.dpf.common.operationparser.internal;

public abstract class BinaryOperatorExpression extends Expression {
	protected final Expression mLeft;
	protected final Expression mRight;

	public BinaryOperatorExpression(final Expression aLeft, final Expression aRight) {
		mLeft = aLeft;
		mRight = aRight;
	}
}
