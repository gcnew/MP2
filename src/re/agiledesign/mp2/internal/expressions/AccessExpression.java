package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class AccessExpression extends Expression {
	private final String mVariableName;

	public AccessExpression(final String aVariableName) {
		mVariableName = aVariableName;
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getVariable(mVariableName);
	}
}
