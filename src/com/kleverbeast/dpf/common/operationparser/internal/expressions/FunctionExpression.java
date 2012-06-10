package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import java.util.ArrayList;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class FunctionExpression extends Expression {
	private final String mFunctionName;
	private final List<Expression> mArgs;

	public FunctionExpression(final String aFunctionName, final List<Expression> aArgs) {
		mFunctionName = aFunctionName;
		mArgs = aArgs;
	}

	public Object execute(final Scope aScope) throws Exception {
		final List<Object> args = new ArrayList<Object>(mArgs.size());

		for (final Expression e : mArgs) {
			args.add(e.execute(aScope));
		}

		// call the function
		mFunctionName.charAt(0);
		return null;
	}
}
