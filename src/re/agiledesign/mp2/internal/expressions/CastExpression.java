package re.agiledesign.mp2.internal.expressions;

import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.util.CoercionUtil;
import re.agiledesign.mp2.util.CoercionUtil.CoercionType;

public class CastExpression extends Expression {
	private final CoercionType mType;
	private final Expression mExpression;

	public CastExpression(final CoercionType aType, final Expression aExpression) {
		mType = aType;
		mExpression = aExpression;
	}

	public Object execute(final Scope aScope) throws Exception {
		return CoercionUtil.cast(mExpression.execute(aScope), mType);
	}

	public CoercionType getType() {
		return mType;
	}
}
