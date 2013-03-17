package re.agiledesign.mp2.internal.statements;

import re.agiledesign.mp2.internal.ControlFlow;
import re.agiledesign.mp2.internal.Scope;

public class BreakStatement extends Statement {
	public void execute(final Scope aScope) throws Exception {
		aScope.setControlFlow(ControlFlow.BREAK);
	}
}
