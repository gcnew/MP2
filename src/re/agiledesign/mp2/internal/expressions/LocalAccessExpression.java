package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;

public class LocalAccessExpression extends Expression {
	private final int mIndex;

	public LocalAccessExpression(final int aIndex) {
		mIndex = aIndex;
	}

	@Override
	public Object execute(final Scope aScope) throws Exception {
		return aScope.getLocalVariable(mIndex);
	}
}
