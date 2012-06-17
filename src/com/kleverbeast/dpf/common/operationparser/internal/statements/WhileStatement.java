package com.kleverbeast.dpf.common.operationparser.internal.statements;

import static com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.isTrue;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;

public class WhileStatement extends Statement {
	private final Expression mCondition;
	private final Statement mBody;

	public WhileStatement(final Expression aCondition, final Statement aBody) {
		mCondition = aCondition;
		mBody = aBody;
	}

	public void execute(final Scope aScope) throws Exception {
		while (isTrue(mCondition.execute(aScope))) {
			mBody.execute(aScope);

			if (aScope.getControlFlow() != null) {
				switch (aScope.getControlFlow().getType()) {
				case BREAK:
					aScope.setControlFlow(null);
					return;
				case CONTINUE:
					aScope.setControlFlow(null);
					break;
				case RETURN:
					return;
				}
			}
		}
	}
}
