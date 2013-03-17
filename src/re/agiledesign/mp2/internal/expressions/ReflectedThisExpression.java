package re.agiledesign.mp2.internal.expressions;

import java.util.List;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.ReflectionUtil;

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

		return ReflectionUtil.invokeMethod(_this, mMethodName, aScope, mArguments);
	}
}
