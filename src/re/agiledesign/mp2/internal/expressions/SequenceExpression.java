package re.agiledesign.mp2.internal.expressions;

import java.util.List;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.AssertUtil;

public class SequenceExpression extends Expression {
	private final List<Expression> mExpressions;

	public SequenceExpression(final List<Expression> aExpressions) {
		AssertUtil.runtimeAssert(!aExpressions.isEmpty());

		mExpressions = aExpressions;
	}

	public Object execute(Scope aScope) throws Exception {
		Object retval = null;

		for (final Expression expr : mExpressions) {
			retval = expr.execute(aScope);
		}

		return retval;
	}
}
