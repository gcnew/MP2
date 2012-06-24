package com.kleverbeast.dpf.common.operationparser.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.kleverbeast.dpf.common.operationparser.internal.expressions.ArgumentAccessExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.ArgumentAssignmentExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.ConstantExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.LocalAccessExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.LocalAssignmentExpression;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Token;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenConstants;

public class ExpressionFactory {
	private static final ConstantExpression NULL = new ConstantExpression(TokenConstants.getKeywordToken("null"));
	private final Map<Object, ConstantExpression> mConstantCache = new HashMap<Object, ConstantExpression>();
	private LocalAccessExpression mLocalAccessCache[] = new LocalAccessExpression[16];
	private LocalAssignmentExpression mLocalAsignCache[] = new LocalAssignmentExpression[16];
	private ArgumentAccessExpression mArgumentAccessCache[] = new ArgumentAccessExpression[16];
	private ArgumentAssignmentExpression mArgumentAssignCache[] = new ArgumentAssignmentExpression[16];

	public ConstantExpression getConstantExpression(final Token aToken) {
		return getConstantExpression(aToken.getValue());
	}

	public ConstantExpression getConstantExpression(final Object aObject) {
		if (aObject == null) {
			return NULL;
		}

		ConstantExpression retval = mConstantCache.get(aObject);
		if (retval == null) {
			retval = new ConstantExpression(aObject);
			mConstantCache.put(aObject, retval);
		}

		return retval;
	}

	public LocalAccessExpression getLocalAccessExpression(final int aIndex) {
		if (aIndex > mLocalAccessCache.length) {
			int newSize = mLocalAccessCache.length;
			do {
				newSize *= 2;
			} while (newSize < aIndex);

			mLocalAccessCache = Arrays.copyOf(mLocalAccessCache, newSize);
		}

		if (mLocalAccessCache[aIndex] == null) {
			mLocalAccessCache[aIndex] = new LocalAccessExpression(aIndex);
		}

		return mLocalAccessCache[aIndex];
	}

	public LocalAssignmentExpression getLocalAssignmentExpression(final int aIndex, final Expression aExpression) {
		if (aIndex > mLocalAsignCache.length) {
			int newSize = mLocalAsignCache.length;
			do {
				newSize *= 2;
			} while (newSize < aIndex);

			mLocalAsignCache = Arrays.copyOf(mLocalAsignCache, newSize);
		}

		if (mLocalAsignCache[aIndex] == null) {
			mLocalAsignCache[aIndex] = new LocalAssignmentExpression(aIndex, aExpression);
		}

		return mLocalAsignCache[aIndex];
	}

	public ArgumentAccessExpression getArgumentAccessExpression(final int aIndex) {
		if (aIndex > mArgumentAccessCache.length) {
			int newSize = mArgumentAccessCache.length;
			do {
				newSize *= 2;
			} while (newSize < aIndex);

			mArgumentAccessCache = Arrays.copyOf(mArgumentAccessCache, newSize);
		}

		if (mArgumentAccessCache[aIndex] == null) {
			mArgumentAccessCache[aIndex] = new ArgumentAccessExpression(aIndex);
		}

		return mArgumentAccessCache[aIndex];
	}

	public ArgumentAssignmentExpression getArgumentAssignmentExpression(final int aIndex, final Expression aExpression) {
		if (aIndex > mArgumentAssignCache.length) {
			int newSize = mArgumentAssignCache.length;
			do {
				newSize *= 2;
			} while (newSize < aIndex);

			mArgumentAssignCache = Arrays.copyOf(mArgumentAssignCache, newSize);
		}

		if (mArgumentAssignCache[aIndex] == null) {
			mArgumentAssignCache[aIndex] = new ArgumentAssignmentExpression(aIndex, aExpression);
		}

		return mArgumentAssignCache[aIndex];
	}

	public static final ConstantExpression getNull() {
		return NULL;
	}
}
