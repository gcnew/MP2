package re.agiledesign.mp2.internal.expressions;

import static re.agiledesign.mp2.util.Util.getClassString;

import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.exception.ScriptException;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.ArrayUtil;
import re.agiledesign.mp2.util.Util;

public class IndexAssignmentExpression extends Expression {
	private final Expression mThis;
	private final Expression mIndex;
	private final Expression mRight;

	public IndexAssignmentExpression(final Expression aThis, final Expression aIndex, final Expression aRight) {
		mThis = aThis;
		mIndex = aIndex;
		mRight = aRight;
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object _this = mThis.execute(aScope);

		if (_this == null) {
			throw new ScriptException("Null pointer indexing");
		}

		final Object index = mIndex.execute(aScope);
		final Object right = mRight.execute(aScope);
		if (_this.getClass().isArray()) {
			if (!(index instanceof Number)) {
				throw new ScriptException("Numeric index expected but found: " + getClassString(index));
			}

			// TODO: fix for float and higher than int precisions
			return ArrayUtil.setIndex(_this, ((Number) index).intValue(), right);
		}

		if (_this instanceof List) {
			if (!(index instanceof Number)) {
				throw new ScriptException("Numeric index expected but found: " + getClassString(index));
			}

			// TODO: fix for float
			Util.<List<Object>> cast(_this).set(((Number) index).intValue(), right);
			return right;
		}

		if (_this instanceof Map) {
			Util.<Map<Object, Object>> cast(_this).put(index, right);
			return right;
		}

		throw new ScriptException("Operator [] not applicable for types(" + getClassString(_this) + ", "
				+ getClassString(index) + ", " + getClassString(right) + ")");
	}
}
