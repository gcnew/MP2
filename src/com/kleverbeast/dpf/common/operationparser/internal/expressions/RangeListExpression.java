package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import java.util.Collections;

import javax.script.ScriptException;

import com.kleverbeast.dpf.common.operationparser.collection.RangeList;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class RangeListExpression extends Expression {
	private final Expression mTo;
	private final Expression mFrom;

	public RangeListExpression(final Expression aFrom) {
		this(aFrom, null);
	}

	public RangeListExpression(final Expression aFrom, final Expression aTo) {
		mFrom = aFrom;
		mTo = aTo;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object from = mFrom.execute(aScope);

		if (!(from instanceof Number)) {
			throw new ScriptException("Numeric from index expected but found: " + getClassString(from));
		}

		final int iTo;
		final int iFrom = ((Number) from).intValue();
		if (mTo == null) {
			iTo = Integer.MAX_VALUE - 1;
		} else {
			final Object to = mTo.execute(aScope);

			if (!(to instanceof Number)) {
				throw new ScriptException("Numeric to index expected but found: " + getClassString(to));
			}

			iTo = ((Number) to).intValue();
		}

		if (iTo < iFrom) {
			return Collections.emptyList();
		}

		return new RangeList(iFrom, iTo);
	}
}
