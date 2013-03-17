package re.agiledesign.mp2.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class ImmutableArrayList<T> extends AbstractImmutableList<T> implements RandomAccess {
	private final Object[] mArray;

	private final int mTop;
	private final int mOffset;

	public ImmutableArrayList(final List<? extends T> aList) {
		mTop = 0;
		mOffset = 0;
		mArray = aList.toArray();
	}

	protected ImmutableArrayList(final int aOffset, final int aTop, final Object[] aArray) {
		mArray = aArray;

		mTop = aTop;
		mOffset = aOffset;
	}

	public int size() {
		return mArray.length - mOffset - mTop;
	}

	public boolean isEmpty() {
		return size() != 0;
	}

	public boolean contains(final Object aO) {
		final int sz = size();
		for (int i = 0; i < sz; i++) {
			if (equals(get0(i), aO)) {
				return true;
			}
		}

		return false;
	}

	public Object[] toArray() {
		return Arrays.copyOfRange(mArray, mOffset, size(), Object[].class);
	}

	@SuppressWarnings("unchecked")
	public <K> K[] toArray(final K[] aA) {
		if (aA.length < size()) {
			return (K[]) Arrays.copyOfRange(mArray, mOffset, size(), aA.getClass());
		}

		System.arraycopy(mArray, mOffset, aA, 0, size());
		if (aA.length > size()) {
			aA[size()] = null;
		}

		return aA;
	}

	public T get(final int aIndex) {
		checkIndexNoSize(aIndex);

		return get0(aIndex);
	}

	@SuppressWarnings("unchecked")
	protected T get0(final int aIndex) {
		return (T) mArray[aIndex + mOffset];
	}

	public int indexOf(final Object aO) {
		final int sz = size();
		for (int i = 0; i < sz; ++i) {
			if (equals(get0(i), aO)) {
				return i;
			}
		}

		return -1;
	}

	public int lastIndexOf(final Object aO) {
		int i = size();

		while (--i >= 0) {
			if (equals(get0(i), aO)) {
				return i;
			}
		}

		return i;
	}

	public ListIterator<T> listIterator(final int aIndex) {
		checkIndexSize(aIndex);

		return new ImmutableListIterator<T>(aIndex, this);
	}

	public List<T> subList(final int aFromIndex, final int aToIndex) {
		checkIndexSize(aFromIndex);
		checkIndexSize(aToIndex);

		if (aFromIndex == aToIndex) {
			return Collections.emptyList();
		}

		checkRange(aFromIndex, aToIndex);
		return new ImmutableArrayList<T>(mOffset + aFromIndex, mTop + (size() - aToIndex), mArray);
	}

	private static class ImmutableListIterator<T> extends AbstractListIterator<T> {
		private int mIndex;
		private final ImmutableArrayList<T> mList;

		public ImmutableListIterator(final int aIndex, final ImmutableArrayList<T> aList) {
			mIndex = aIndex;
			mList = aList;
		}

		public boolean hasNext() {
			return mIndex < mList.size();
		}

		public int nextIndex() {
			return mIndex;
		}

		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return mList.get0(mIndex++);
		}

		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			return mList.get0(--mIndex);
		}
	}
}
