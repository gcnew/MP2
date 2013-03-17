package re.agiledesign.mp2.internal.expressions;

import java.util.List;

import re.agiledesign.mp2.exception.FunctionNotFound;
import re.agiledesign.mp2.exception.NotAFunction;
import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.NameScope;
import re.agiledesign.mp2.internal.Scope;

public class FunctionCallExpression extends Expression {
	private final Expression mFunctionAccessor;
	private final List<Expression> mArgs;

	public FunctionCallExpression(final Expression aFunctionAccessor, final List<Expression> aArgs) {
		mFunctionAccessor = aFunctionAccessor;
		mArgs = aArgs;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object var = mFunctionAccessor.execute(aScope);

		if (var == null) {
			throw new FunctionNotFound("unknown");
		}

		if (!(var instanceof FunctionExpression)) {
			throw new NotAFunction("unknown");
		}

		int i = 0;
		final Object args[] = new Object[mArgs.size()];
		for (final Expression e : mArgs) {
			args[i++] = e.execute(aScope);
		}

		final FunctionExpression function = (FunctionExpression) var;
		final NameScope parent = (aScope instanceof FunctionScope) ? aScope.getPrevious() : (NameScope) aScope;
		final FunctionScope scope = new FunctionScope(parent, args, function.getLocalsCount());

		return function.execute(scope);
	}
}
