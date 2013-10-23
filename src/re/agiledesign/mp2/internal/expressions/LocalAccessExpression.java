package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class LocalAccessExpression extends AccessExpression {
	private final int mIndex;

	public LocalAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	public Expression asAssignment(final Expression aRight) {
		return new LocalAssignmentExpression(mIndex, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getLocalVariable(mIndex);
	}
}
