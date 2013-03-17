package re.agiledesign.mp2.internal.statements;

import re.agiledesign.mp2.internal.Scope;

public abstract class Statement {
	public abstract void execute(final Scope aScope) throws Exception;
}
