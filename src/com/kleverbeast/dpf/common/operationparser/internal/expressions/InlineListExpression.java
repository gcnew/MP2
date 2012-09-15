package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import java.util.ArrayList;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class InlineListExpression extends Expression {
	private final List<Expression> mElements;

	public InlineListExpression(final List<Expression> aElements) {
		mElements = aElements;
	}

	public Object execute(final Scope aScope) throws Exception {
		final List<Object> retval = new ArrayList<Object>(mElements.size());

		for (final Expression e : mElements) {
			retval.add(e.execute(aScope));
		}

		return retval;
	}
}
