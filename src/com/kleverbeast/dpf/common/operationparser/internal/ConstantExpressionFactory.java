package com.kleverbeast.dpf.common.operationparser.internal;

import java.util.HashMap;
import java.util.Map;

import com.kleverbeast.dpf.common.operationparser.internal.expressions.ConstantExpression;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Token;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenConstants;

public class ConstantExpressionFactory {
	private static final ConstantExpression mNull = new ConstantExpression(TokenConstants.getKeywordToken("null"));
	private final Map<Object, ConstantExpression> mCache = new HashMap<Object, ConstantExpression>();

	public ConstantExpression getExpression(final Token aToken) {
		return getExpression(aToken.getValue());
	}

	public ConstantExpression getExpression(final Object aObject) {
		if (aObject == null) {
			return mNull;
		}

		ConstantExpression retval = mCache.get(aObject);
		if (retval == null) {
			retval = new ConstantExpression(aObject);
			mCache.put(aObject, retval);
		}

		return retval;
	}
}
