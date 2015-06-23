package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.ReflectionUtil;

public class ReflectedApplyExpression extends Expression {
	private final Expression mThis;
	private final String mMethodName;

	public ReflectedApplyExpression(final Expression aThis, final String aMethodName) {
		mThis = aThis;
		mMethodName = aMethodName.intern();
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);
		final Object[] args = ((FunctionScope) aScope).getArguments();

		boolean noNull = true;
		final Class<?>[] types = new Class[args.length];
		for (int i = 0; i < args.length; ++i) {
			if (args[i] == null) {
				noNull = false;
			} else {
				types[i] = args[i].getClass();
			}
		}

		return ReflectionUtil.invokeMethod(_this, mMethodName, args, types, noNull);
	}
}
