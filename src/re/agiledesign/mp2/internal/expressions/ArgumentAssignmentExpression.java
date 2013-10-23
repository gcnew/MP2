package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;

public class ArgumentAssignmentExpression extends Expression {
	private final int mIndex;
	private final Expression mExpression;

	public ArgumentAssignmentExpression(final int aIndex, final Expression aExpression) {
		mIndex = aIndex;
		mExpression = aExpression;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object value = mExpression.execute(aScope);
		((FunctionScope) aScope).setArgument(mIndex, value);

		return value;
	}
}
