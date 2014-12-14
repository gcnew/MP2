package re.agiledesign.mp2.collection;

public class ConsList<T> extends AbstractConsList<T> {
	private final T mFirst;
	private final SequentialList<T> mRest;

	public ConsList(final T aValue) {
		this(aValue, null);
	}

	public ConsList(final T aValue, final SequentialList<T> aRest) {
		mFirst = aValue;
		mRest = aRest;
	}

	public static <T> ConsList<T> list(final T... aO) {
		if (aO == null) {
			return null;
		}

		return list(0, aO);
	}

	private static <T> ConsList<T> list(final int aOffset, final T[] aO) {
		if (aOffset == aO.length) {
			return null;
		}

		return new ConsList<T>(aO[aOffset], list(aOffset + 1, aO));
	}

	public final T first() {
		return mFirst;
	}

	public final SequentialList<T> rest() {
		return mRest;
	}

	public ConsList<T> cons(final T aValue) {
		return new ConsList<T>(aValue, this);
	}
}
