package com.kleverbeast.dpf.common.operationparser.internal.statements;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.isTrue;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class ForStatement extends Statement {
	private final Expression mExpr1;
	private final Expression mExpr2;
	private final Expression mExpr3;
	private final Statement mBody;

	public ForStatement(final Expression aExpr1, final Expression aExpr2, final Expression aExpr3, final Statement aBody) {
		mExpr1 = aExpr1;
		mExpr2 = aExpr2;
		mExpr3 = aExpr3;
		mBody = aBody;
	}

	public void execute(final Scope aScope) throws Exception {
		mExpr1.execute(aScope);

		while (isTrue(mExpr2.execute(aScope))) {
			mBody.execute(aScope);
			mExpr3.execute(aScope);
		}
	}
}
