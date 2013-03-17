package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.tokenizer.Token;

public class ConstantExpression extends Expression {
	private final Object mConstant;

	public ConstantExpression(final Object aObject) {
		mConstant = aObject;
	}

	public ConstantExpression(final Token aToken) {
		mConstant = aToken.getValue();
	}

	public Object execute(Scope aScope) {
		return mConstant;
	}
}
