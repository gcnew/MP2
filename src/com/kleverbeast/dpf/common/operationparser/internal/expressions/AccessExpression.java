package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class AccessExpression extends Expression {
	private final String mVariableName;

	public AccessExpression(final String aVariableName) {
		mVariableName = aVariableName;
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getVariable(mVariableName);
	}
}
