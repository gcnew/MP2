package re.agiledesign.mp2.internal.statements;

import java.util.List;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;


public class SequenceStatement extends Statement {
	private final List<Expression> mExpressions;

	public SequenceStatement(final List<Expression> aExpressions) {
		mExpressions = aExpressions;
	}

	public void execute(final Scope aScope) throws Exception {
		for (final Expression e : mExpressions) {
			e.execute(aScope);
		}
	}
}
