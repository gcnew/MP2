package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class GlobalAssignmentExpression extends Expression {
	private final String mVariableName;
	private final Expression mExpression;

	public GlobalAssignmentExpression(final String aVariableName, final Expression aExpression) {
		mVariableName = aVariableName;
		mExpression = aExpression;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object retval = mExpression.execute(aScope);

		aScope.setVariable(mVariableName, retval);
		return retval;
	}
}
