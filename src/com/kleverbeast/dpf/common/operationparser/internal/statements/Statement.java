package com.kleverbeast.dpf.common.operationparser.internal.statements;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public abstract class Statement {
	public abstract void execute(final Scope aScope) throws Exception;
}
