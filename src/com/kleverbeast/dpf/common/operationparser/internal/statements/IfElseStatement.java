package com.kleverbeast.dpf.common.operationparser.internal.statements;

import static com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.isTrue;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class IfElseStatement extends Statement {
	private final Expression mCondition;
	private final Statement mTrueStatement;
	private final Statement mFalseStatement;

	public IfElseStatement(final Expression aCondition, final Statement aTrueStatement) {
		this(aCondition, aTrueStatement, null);
	}

	public IfElseStatement(final Expression aCondition, final Statement aTrueStatement, final Statement aFalseStatement) {
		mCondition = aCondition;
		mTrueStatement = aTrueStatement;
		mFalseStatement = aFalseStatement;
	}

	@Override
	public void execute(final Scope aScope) throws Exception {
		if (isTrue(mCondition.execute(aScope))) {
			mTrueStatement.execute(aScope);
		} else {
			if (mFalseStatement != null) {
				mFalseStatement.execute(aScope);
			}
		}
	}
}
