package re.agiledesign.mp2.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.internal.expressions.ArgumentAccessExpression;
import re.agiledesign.mp2.internal.expressions.ConstantExpression;
import re.agiledesign.mp2.internal.expressions.LocalAccessExpression;
import re.agiledesign.mp2.lexer.SourcePosition;
import re.agiledesign.mp2.lexer.Token;
import re.agiledesign.mp2.lexer.TokenConstants;

public class ExpressionFactory {
	private static final ConstantExpression ONE = new ConstantExpression(Integer.valueOf(1));
	private static final ConstantExpression NULL = new ConstantExpression(TokenConstants.getKeywordToken("null",
			SourcePosition.UNKNOWN));

	private final Map<Object, ConstantExpression> mConstantCache = new HashMap<Object, ConstantExpression>();
	private LocalAccessExpression mLocalAccessCache[] = new LocalAccessExpression[16];
	private ArgumentAccessExpression mArgumentAccessCache[] = new ArgumentAccessExpression[16];

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

	public static final ConstantExpression getNull() {
		return NULL;
	}

	public static final ConstantExpression getOne() {
		return ONE;
	}
}
