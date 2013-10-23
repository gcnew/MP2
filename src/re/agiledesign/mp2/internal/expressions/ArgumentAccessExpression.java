package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;

public class ArgumentAccessExpression extends AccessExpression {
	private final int mIndex;

	public ArgumentAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	public Expression asAssignment(final Expression aRight) {
		return new ArgumentAssignmentExpression(mIndex, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return ((FunctionScope) aScope).getArgument(mIndex);
	}
}
