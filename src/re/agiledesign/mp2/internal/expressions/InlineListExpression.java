package re.agiledesign.mp2.internal.expressions;

import java.util.ArrayList;
import java.util.List;

import re.agiledesign.mp2.collection.ImmutableArrayList;
import re.agiledesign.mp2.internal.Scope;

public class InlineListExpression extends Expression {
	private final boolean mImmutable;
	private final List<Expression> mElements;

	public InlineListExpression(final List<Expression> aElements, final boolean aImmutable) {
		mElements = aElements;
		mImmutable = aImmutable;
	}

	public Object execute(final Scope aScope) throws Exception {
		final List<Object> retval = new ArrayList<Object>(mElements.size());

		for (final Expression e : mElements) {
			retval.add(e.execute(aScope));
		}

		return (mImmutable) ? new ImmutableArrayList<Object>(retval) : retval;
	}
}
