package com.kleverbeast.dpf.common.operationparser.internal;

public class AssignmentExpression extends Expression {
	private final String mVariableName;
	private final Expression mExpression;

	public AssignmentExpression(final String aVariableName, final Expression aExpression) {
		mVariableName = aVariableName;
		mExpression = aExpression;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object retval = mExpression.execute(aScope);

		aScope.put(mVariableName, retval);
		return retval;
	}
}
