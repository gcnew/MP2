package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.AssignmentVisitor;
import re.agiledesign.mp2.internal.Scope;

public class GlobalAccessExpression extends AccessExpression {
	private final String mVariableName;

	public GlobalAccessExpression(final String aVariableName) {
		mVariableName = aVariableName;
	}

	public void visit(final AssignmentVisitor aVisitor) {
		aVisitor.visitGlobalAssignment(mVariableName);
	}

	public Object execute(final Scope aScope) throws Exception {
		return aScope.getVariable(mVariableName);
	}
}
