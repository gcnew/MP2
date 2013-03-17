package re.agiledesign.mp2.internal.expressions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.AssocEntry;


public class InlineMapExpression extends Expression {
	private final boolean mImmutable;
	private final List<AssocEntry<Expression, Expression>> mElements;

	public InlineMapExpression(final List<AssocEntry<Expression, Expression>> aElements, final boolean aImmutable) {
		mImmutable = aImmutable;
		mElements = aElements;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Map<Object, Object> retval = new HashMap<Object, Object>();

		for (final AssocEntry<Expression, Expression> e : mElements) {
			retval.put(e.getKey().execute(aScope), e.getValue().execute(aScope));
		}

		return (mImmutable) ? Collections.unmodifiableMap(retval) : retval;
	}
}
