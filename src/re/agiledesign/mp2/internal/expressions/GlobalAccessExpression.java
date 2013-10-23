package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class GlobalAccessExpression extends AccessExpression {
	private final String mVariableName;

	public GlobalAccessExpression(final String aVariableName) {
		mVariableName = aVariableName;
	}

	public Expression asAssignment(final Expression aRight) {
		return new GlobalAssignmentExpression(mVariableName, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getVariable(mVariableName);
	}
}
