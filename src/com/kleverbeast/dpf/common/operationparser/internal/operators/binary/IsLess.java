package com.kleverbeast.dpf.common.operationparser.internal.operators.binary;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_LESS;


import com.kleverbeast.dpf.common.operationparser.internal.BinaryOperatorExpression;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil;
import com.kleverbeast.dpf.common.operationparser.internal.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class IsLess extends BinaryOperatorExpression {
	public IsLess(final Expression aLeft, final Expression aRight) {
		super(aLeft, aRight);
	}

	public Object execute(Scope aScope) throws Exception {
		final Object left = mLeft.execute(aScope);
		final Object right = mRight.execute(aScope);

		return CoercionUtil.doOperation(IS_LESS, left, right);
	}
}