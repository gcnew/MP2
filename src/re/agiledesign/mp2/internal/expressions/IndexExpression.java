package re.agiledesign.mp2.internal.expressions;

import static re.agiledesign.mp2.util.Util.getClassString;

import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.exception.ScriptException;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.ArrayUtil;

public class IndexExpression extends Expression {
	private final Expression mThis;
	private final Expression mIndex;

	public IndexExpression(final Expression aThis, final Expression aIndex) {
		mThis = aThis;
		mIndex = aIndex;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);

		if (_this == null) {
			throw new ScriptException("Null pointer indexing");
		}

		final Object index = mIndex.execute(aScope);
		if (_this.getClass().isArray()) {
			if (!(index instanceof Number)) {
				throw new ScriptException("Numeric index expected but found: " + getClassString(index));
			}

			// TODO: fix for float and higher than int precisions
			return ArrayUtil.atIndex(_this, ((Number) index).intValue());
		}

		if (_this instanceof List) {
			if (!(index instanceof Number)) {
				throw new ScriptException("Numeric index expected but found: " + getClassString(index));
			}

			// TODO: fix for float
			return ((List<?>) _this).get(((Number) index).intValue());
		}

		if (_this instanceof Map) {
			return ((Map<?, ?>) _this).get(index);
		}

		if (_this instanceof CharSequence) {
			if (!(index instanceof Number)) {
				throw new ScriptException("Numeric index expected but found: " + getClassString(index));
			}

			// TODO: fix for float
			return Character.valueOf(((CharSequence) _this).charAt(((Number) index).intValue()));
		}

		throw new ScriptException("Operator [] not applicable for types(" + getClassString(_this) + ", "
				+ getClassString(index) + ")");
	}
}
