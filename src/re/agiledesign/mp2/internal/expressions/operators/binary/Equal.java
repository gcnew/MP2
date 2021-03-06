package re.agiledesign.mp2.internal.expressions.operators.binary;

import static re.agiledesign.mp2.util.CoercionUtil.isFloating;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.util.ArrayUtil;

public class Equal extends BinaryOperatorExpression {
	public Equal(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(final Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		if (left == right) {
			return Boolean.TRUE;
		}

		if ((left == null) || (right == null)) {
			return Boolean.FALSE;
		}

		if (left.getClass().isArray() || right.getClass().isArray()) {
			return Boolean.valueOf(ArrayUtil.equals(left, right));
		}

		if (left.equals(right)) {
			return Boolean.TRUE;
		}

		if ((left instanceof Number) && (right instanceof Number)) {
			final boolean flt = isFloating((Number) left) || isFloating((Number) right);

			if (flt) {
				return Boolean.valueOf(((Number) left).doubleValue() == ((Number) right).doubleValue());
			}

			return Boolean.valueOf(((Number) left).longValue() == ((Number) right).longValue());
		}

		return Boolean.FALSE;
	}
}
