package re.agiledesign.mp2.internal.statements;

import static re.agiledesign.mp2.util.CoercionUtil.isTrue;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.expressions.Expression;

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
