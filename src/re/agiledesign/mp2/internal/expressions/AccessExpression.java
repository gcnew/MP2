package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.AssignmentVisitor;

public abstract class AccessExpression extends Expression {
	public abstract void visit(final AssignmentVisitor aVisitor);
}
