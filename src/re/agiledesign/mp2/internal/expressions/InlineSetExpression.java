package re.agiledesign.mp2.internal.expressions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import re.agiledesign.mp2.internal.Scope;

public class InlineSetExpression extends Expression {
	private final boolean mImmutable;
	private final List<Expression> mElements;

	public InlineSetExpression(final List<Expression> aElements, final boolean aImmutable) {
		mImmutable = aImmutable;
		mElements = aElements;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Set<Object> retval = new HashSet<Object>();

		for (final Expression e : mElements) {
			retval.add(e.execute(aScope));
		}

		return (mImmutable) ? Collections.unmodifiableSet(retval) : retval;
	}
}
