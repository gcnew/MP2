package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;

public class ArgumentAccessExpression extends Expression {
	private final int mIndex;

	public ArgumentAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		return ((FunctionScope) aScope).getArgument(mIndex);
	}
}
