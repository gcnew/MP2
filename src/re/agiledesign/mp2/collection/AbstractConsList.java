package com.kleverbeast.dpf.common.operationparser.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public abstract class AbstractConsList<T> extends AbstractImmutableList<T> {
	public abstract T first();

	public abstract AbstractConsList<T> rest();

	public int size() {
		return (rest() == null) ? 1 : (rest().size() + 1);
	}

	public boolean isEmpty() {
		return false;
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

	public boolean contains(final Object aO) {
		if (equals(first(), aO)) {
			return true;
		}

		return (rest() != null) && rest().contains(aO);
	}

	public Object[] toArray() {
		return toArrayList().toArray();
	}

	public <K> K[] toArray(final K[] aArray) {
		return toArrayList().toArray(aArray);
	}

	private ArrayList<T> toArrayList() {
		final ArrayList<T> retval = new ArrayList<T>();

		AbstractConsList<T> l = this;
		do {
			retval.add(l.first());
			l = l.rest();
		} while (l != null);

		return retval;
	}

	public T get(final int aIndex) {
		if (aIndex < 0) {
			throw new IndexOutOfBoundsException("Negative index passed");
		}

		return get0(aIndex, aIndex);
	}

	private T get0(final int aIndex, final int aOrigIndex) {
		if (aIndex == 0) {
			return first();
		}

		if (rest() == null) {
			throwIndexGtSize(aOrigIndex);
		}

		return rest().get0(aIndex - 1, aOrigIndex);
	}

	private void throwIndexGtSize(final int aIndex) {
		throw new IndexOutOfBoundsException("Index: " + aIndex + " greater than list size");
	}

	public int indexOf(final Object aO) {
		if (equals(first(), aO)) {
			return 0;
		}

		final int retval = (rest() != null) ? rest().indexOf(aO) : -1;
		return (retval < 0) ? -1 : (1 + retval);
	}

	public int lastIndexOf(final Object aO) {
		final int retval = (rest() != null) ? rest().indexOf(aO) : -1;

		return (retval >= 0) ? retval : (equals(first(), aO) ? 0 : -1);
	}

	public ListIterator<T> listIterator(final int aIndex) {
		final AbstractConsList<T> list = (aIndex == 0) ? this : subList0(aIndex, aIndex, -1);

		return new ForwardOnlyListIterator<T>(list);
	}

	public List<T> subList(final int aFromIndex, final int aToIndex) {
		if (aFromIndex < 0) {
			throw new IndexOutOfBoundsException("Negative From index passed");
		}

		if (aFromIndex == aToIndex) {
			return Collections.emptyList();
		}

		if (aFromIndex > aToIndex) {
			throw new IndexOutOfBoundsException("From index less than To index: " + aFromIndex + " -> " + aToIndex);
		}

		return subList0(aFromIndex, aFromIndex, aToIndex);
	}

	public AbstractConsList<T> subList2(final int aFromIndex, final int aToIndex) {
		if (aFromIndex < 0) {
			throw new IndexOutOfBoundsException("Negative From index passed");
		}

		if (aFromIndex == aToIndex) {
			return null;
		}

		if ((aToIndex >= 0) && (aFromIndex > aToIndex)) {
			throw new IndexOutOfBoundsException("From index less than To index: " + aFromIndex + " -> " + aToIndex);
		}

		return subList0(aFromIndex, aFromIndex, aToIndex);
	}

	private AbstractConsList<T> subList0(final int aFromIndex, final int aFromOrig, final int aToIndex) {
		if (aToIndex < 0) {
			if (aFromIndex == 0) {
				return this;
			}

			if (aFromIndex == 1) {
				return rest();
			}

			if (rest() == null) {
				throwIndexGtSize(aFromOrig);
			}

			return rest().subList0(aFromIndex - 1, aFromOrig, aToIndex);
		}

		if (aFromIndex > 0) {
			if (rest() == null) {
				throw new IllegalStateException("To index greater than list length");
			}

			return rest().subList0(aFromIndex - 1, aFromOrig, aToIndex - 1);
		}

		return copy(this, aToIndex);
	}

	private static <T> AbstractConsList<T> copy(final AbstractConsList<T> aList, final int aN) {
		if (aN == 0) {
			return null;
		}

		if (aList == null) {
			throw new IllegalStateException("List too short");
		}

		return new ConsList<T>(aList.first(), copy(aList.rest(), aN - 1));
	}

	public String toString() {
		return "(" + first() + " : " + rest() + ")";
	}

	private static class ForwardOnlyListIterator<T> extends AbstractListIterator<T> {
		private int mIndex;
		private AbstractConsList<T> mList;
		private AbstractConsList<T> mCurrent;

		public ForwardOnlyListIterator(final AbstractConsList<T> aList) {
			mList = aList;
			mCurrent = aList;
		}

		public boolean hasNext() {
			return mCurrent != null;
		}

		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			final T retval = mCurrent.first();
			mCurrent = mCurrent.rest();
			++mIndex;

			return retval;
		}

		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			--mIndex;
			mCurrent = mList.subList0(mIndex, mIndex, -1);
			return mCurrent.first();
		}

		public int nextIndex() {
			return mIndex;
		}
	}
}
