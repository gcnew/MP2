package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptException;

import com.kleverbeast.dpf.common.operationparser.collection.RangeList;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class RangeListExpression extends Expression {
	private final Expression mTo;
	private final Expression mFrom;
	private final boolean mLazy;

	public RangeListExpression(final Expression aFrom) {
		this(aFrom, null, true);
	}

	public RangeListExpression(final Expression aFrom, final Expression aTo, final boolean aLazy) {
		mFrom = aFrom;
		mTo = aTo;
		mLazy = aLazy;
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

		if (mLazy) {
			return (iTo < iFrom) ? Collections.emptyList() : new RangeList(iFrom, iTo);
		}

		final List<Object> retval = new ArrayList<Object>(1 + iTo - iFrom);
		for (int i = iFrom; i <= iTo; ++i) {
			retval.add(Integer.valueOf(i));
		}

		return retval;
	}
}
