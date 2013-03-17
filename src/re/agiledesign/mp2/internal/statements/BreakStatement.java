package com.kleverbeast.dpf.common.operationparser.internal.statements;

import com.kleverbeast.dpf.common.operationparser.internal.ControlFlow;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class BreakStatement extends Statement {
	public void execute(final Scope aScope) throws Exception {
		aScope.setControlFlow(ControlFlow.BREAK);
	}
}
