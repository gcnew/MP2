package re.agiledesign.mp2.internal.expressions;

import java.util.List;

import re.agiledesign.mp2.internal.ControlFlow.Type;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.statements.Statement;

public class FunctionExpression extends Expression {
	private final Statement mBody;
	private final int mLocalsCount;
	private final List<String> mArguments;

	public FunctionExpression(final Statement aBody, final int aLocalsCount, final List<String> aArguments) {
		mBody = aBody;
		mLocalsCount = aLocalsCount;
		mArguments = aArguments;
	}

	public Object execute(final Scope aScope) throws Exception {
		mBody.execute(aScope);

		if ((aScope.getControlFlow() != null) && (aScope.getControlFlow().getType() == Type.RETURN)) {
			return aScope.getControlFlow().getValue();
		}

		return null;
	}

	public List<String> getArguments() {
		return mArguments;
	}

	public int getLocalsCount() {
		return mLocalsCount;
	}
}
