package com.kleverbeast.dpf.common.operationparser.internal;

public abstract class Statement {
	public abstract void execute(final Scope aScope) throws Exception;
}
