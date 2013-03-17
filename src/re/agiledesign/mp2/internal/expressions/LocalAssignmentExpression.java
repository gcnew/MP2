package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class LocalAssignmentExpression extends Expression {
	private final int mIndex;
	private final Expression mExpression;

	public LocalAssignmentExpression(final int aIndex, final Expression aExpression) {
		mIndex = aIndex;
		mExpression = aExpression;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		final Object value = mExpression.execute(aScope);
		aScope.setLocalVariable(mIndex, value);

		return value;
	}
}
