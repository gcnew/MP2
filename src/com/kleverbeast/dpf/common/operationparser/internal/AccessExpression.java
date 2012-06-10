package com.kleverbeast.dpf.common.operationparser.internal;

public class AccessExpression extends Expression {
	private final String mVariableName;

	public AccessExpression(final String aVariableName) {
		mVariableName = aVariableName;
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getVariable(mVariableName);
	}
}
