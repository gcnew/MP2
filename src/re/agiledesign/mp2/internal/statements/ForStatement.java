package re.agiledesign.mp2.internal.statements;

import static re.agiledesign.mp2.util.CoercionUtil.isTrue;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;


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

			mExpr3.execute(aScope);
		}
	}
}
