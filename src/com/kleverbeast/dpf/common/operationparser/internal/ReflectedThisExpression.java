package com.kleverbeast.dpf.common.operationparser.internal;

import java.lang.reflect.Method;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.Util;

public class ReflectedThisExpression extends Expression {
	private final Expression mThis;
	private final String mMethodName;
	private final List<Expression> mArguments;

	public ReflectedThisExpression(final Expression aThis, final String aMethodName, final List<Expression> aArguments) {
		mThis = aThis;
		mMethodName = aMethodName;
		mArguments = aArguments;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);

		int i = 0;
		final Object args[] = new Object[mArguments.size()];
		final Class<?> argTypes[] = new Class<?>[args.length];
		for (final Expression e : mArguments) {
			args[i] = e.execute(aScope);
			argTypes[i] = args[i].getClass();

			++i;
		}

		final Method m = Util.getMethod(_this, mMethodName, argTypes);
		return m.invoke(_this, args);
	}
}
