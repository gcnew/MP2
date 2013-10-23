package re.agiledesign.mp2.internal.expressions;

public abstract class AccessExpression extends Expression {
	public abstract Expression asAssignment(final Expression aRight);
}
