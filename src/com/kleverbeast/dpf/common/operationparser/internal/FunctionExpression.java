package com.kleverbeast.dpf.common.operationparser.internal;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression extends Expression {
	private final String mFunctionName;
	private final List<Expression> mArgs;

	public FunctionExpression(final String aFunctionName, final List<Expression> aArgs) {
		mFunctionName = aFunctionName;
		mArgs = aArgs;
	}

	public Object execute(Scope aScope) throws Exception {
		final List<Object> args = new ArrayList<Object>(mArgs.size());

		for (Expression e : mArgs) {
			args.add(e.execute(aScope));
		}

		// call the function
		mFunctionName.charAt(0);
		return null;
	}
}
