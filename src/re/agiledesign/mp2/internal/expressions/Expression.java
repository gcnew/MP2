package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public abstract class Expression {
	public abstract Object execute(final Scope aScope) throws Exception;
}
