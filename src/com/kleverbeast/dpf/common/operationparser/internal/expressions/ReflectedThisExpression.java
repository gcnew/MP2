package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.util.ReflectionUtil;

public class ReflectedThisExpression extends Expression {
	private final Expression mThis;
	private final String mMethodName;
	private final List<Expression> mArguments;

	public ReflectedThisExpression(final Expression aThis, final String aMethodName, final List<Expression> aArguments) {
		mThis = aThis;
		mMethodName = aMethodName.intern();
		mArguments = aArguments;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);

		int i = 0;
		final Object args[] = new Object[mArguments.size()];
		for (final Expression e : mArguments) {
			args[i++] = e.execute(aScope);
		}

		return ReflectionUtil.invokeMethod(_this, mMethodName, args);
	}
}
