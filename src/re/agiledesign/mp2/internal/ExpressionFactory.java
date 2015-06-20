package re.agiledesign.mp2.internal;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.WeakHashMap;

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

	private static final WeakHashMap<Object, ConstantExpression> mConstantCache = new WeakHashMap<Object, ConstantExpression>();

	@SuppressWarnings("unchecked")
	private static WeakReference<LocalAccessExpression> mLocalAccessCache[] = new WeakReference[16];

	@SuppressWarnings("unchecked")
	private static WeakReference<ArgumentAccessExpression> mArgumentAccessCache[] = new WeakReference[16];

	public static ConstantExpression getConstantExpression(final Token aToken) {
		return getConstantExpression(aToken.getValue());
	}

	public static ConstantExpression getConstantExpression(final Object aObject) {
		if (aObject == null) {
			return NULL;
		}

		if (aObject == Integer.valueOf(1)) {
			return ONE;
		}

		ConstantExpression retval = mConstantCache.get(aObject);
		if (retval == null) {
			retval = new ConstantExpression(aObject);
			mConstantCache.put(aObject, retval);
		}

		return retval;
	}

	private static <T> T[] ensure(final int aIndex, final T[] aArray) {
		if (aIndex < aArray.length) {
			return aArray;
		}

		int newSize = aArray.length;
		do {
			newSize *= 2;
		} while (newSize <= aIndex);

		return Arrays.copyOf(aArray, newSize);
	}

	public static LocalAccessExpression getLocalAccessExpression(final int aIndex) {
		mLocalAccessCache = ensure(aIndex, mLocalAccessCache);

		final WeakReference<LocalAccessExpression> ref = mLocalAccessCache[aIndex];
		if ((ref == null) || (ref.get() == null)) {
			mLocalAccessCache[aIndex] = new WeakReference<LocalAccessExpression>(new LocalAccessExpression(aIndex));
		}

		return mLocalAccessCache[aIndex].get();
	}

	public static ArgumentAccessExpression getArgumentAccessExpression(final int aIndex) {
		mArgumentAccessCache = ensure(aIndex, mArgumentAccessCache);

		final WeakReference<ArgumentAccessExpression> ref = mArgumentAccessCache[aIndex];
		if ((ref == null) || (ref.get() == null)) {
			mArgumentAccessCache[aIndex] = new WeakReference<ArgumentAccessExpression>(new ArgumentAccessExpression(
				aIndex));
		}

		return mArgumentAccessCache[aIndex].get();
	}

	public static final ConstantExpression getNull() {
		return NULL;
	}

	public static final ConstantExpression getOne() {
		return ONE;
	}
}
