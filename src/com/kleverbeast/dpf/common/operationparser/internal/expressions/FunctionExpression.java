package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import java.util.Collections;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.ControlFlow.Type;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;

public class FunctionExpression extends Expression {
	private final Statement mBody;
	private final int mLocalsCount;
	private final List<String> mArguments;

	public FunctionExpression(final Statement aBody, final int aLocalsCount, final List<String> aArguments) {
		mBody = aBody;
		mLocalsCount = aLocalsCount;
		mArguments = Collections.unmodifiableList(aArguments);
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
