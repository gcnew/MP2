package re.agiledesign.mp2.internal.expressions;

import static re.agiledesign.mp2.util.CoercionUtil.isTrue;
import re.agiledesign.mp2.internal.Scope;


public class TernaryExpression extends Expression {
	private final Expression mCondition;
	private final Expression mTrue;
	private final Expression mFalse;

	public TernaryExpression(final Expression aCondition, final Expression aTrue, final Expression aFalse) {
		mCondition = aCondition;
		mTrue = aTrue;
		mFalse = aFalse;
	}

	public Object execute(final Scope aScope) throws Exception {
		return (isTrue(mCondition.execute(aScope)) ? mTrue : mFalse).execute(aScope);
	}
}
