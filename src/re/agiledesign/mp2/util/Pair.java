package re.agiledesign.mp2.util;

public class Pair<F, S> {
	private final F mFirst;
	private final S mSecond;

	public Pair(final F aFirst, final S aSecond) {
		mFirst = aFirst;
		mSecond = aSecond;
	}

	public F getFirst() {
		return mFirst;
	}

	public S getSecond() {
		return mSecond;
	}

	public int hashCode() {
		int result = 1;
		final int prime = 31;

		result = prime * result + ((mFirst == null) ? 0 : mFirst.hashCode());
		result = prime * result + ((mSecond == null) ? 0 : mSecond.hashCode());

		return result;
	}

	public boolean equals(final Object aOther) {
		if (this == aOther) {
			return true;
		}

		if ((aOther == null) || (this.getClass() != aOther.getClass())) {
			return false;
		}

		final Pair<?, ?> other = (Pair<?, ?>) aOther;
		if ((mFirst != other.mFirst) && (mFirst == null)) {
			return false;
		}

		if ((mSecond != other.mSecond) && (mSecond == null)) {
			return false;
		}

		return mFirst.equals(other.mFirst) && mSecond.equals(other.mSecond);
	}
}
