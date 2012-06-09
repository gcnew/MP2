package com.kleverbeast.dpf.common.operationparser.internal;

public abstract class Expression {
	public abstract Object execute(final Scope aScope) throws Exception;
}
