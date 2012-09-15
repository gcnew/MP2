package com.kleverbeast.dpf.common.operationparser.internal.expressions;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import java.util.List;

import com.kleverbeast.dpf.common.operationparser.exception.ScriptException;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class SublistExpression extends Expression {
	private final Expression mThis;
	private final Expression mFromIdx;
	private final Expression mToIdx;

	public SublistExpression(final Expression aThis, final Expression aFromIdx, final Expression aToIdx) {
		mThis = aThis;
		mFromIdx = aFromIdx;
		mToIdx = aToIdx;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);

		if (_this == null) {
			throw new ScriptException("Null pointer indexing");
		}

		final Object from = mFromIdx.execute(aScope);
		final Object to = mToIdx.execute(aScope);

		if (!((from instanceof Number) && (to instanceof Number))) {
			throw new ScriptException("Sublist: numeric indices expected");
		}
		// TODO: fix for float and higher than int precisions
		final int fromIdx = ((Number) from).intValue();
		final int toIdx = ((Number) to).intValue();

		if (toIdx < 0) {
			throw new ScriptException("Sublist: Negative to index");
		}

		if (fromIdx < 0) {
			throw new ScriptException("Sublist: Negative from index");
		}

		if (toIdx < fromIdx) {
			throw new ScriptException("Sublist: To index is less than from index");
		}

		if (_this.getClass().isArray()) {
			final int length = toIdx - fromIdx;
			final Object retval = new Object[length];

			System.arraycopy(_this, fromIdx, retval, 0, length);
			return retval;
		}

		if (_this instanceof List) {
			return ((List<?>) _this).subList(fromIdx, toIdx);
		}

		if (_this instanceof CharSequence) {
			return ((CharSequence) _this).subSequence(fromIdx, toIdx);
		}

		throw new ScriptException("Operator sequence[x -> y] not applicable for types(" + getClassString(_this) + "["
				+ getClassString(from) + " -> " + getClassString(to) + "])");
	}
}
