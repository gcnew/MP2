package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public abstract class Expression {
	public abstract Object execute(final Scope aScope) throws Exception;
}
