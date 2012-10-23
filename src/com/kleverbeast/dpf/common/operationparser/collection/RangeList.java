package com.kleverbeast.dpf.common.operationparser.collection;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class RangeList extends AbstractImmutableList<Integer> implements RandomAccess {
	private final int mFrom;
	private final int mTo;

	public RangeList(final int aFrom, final int aÒî) {
		checkRange(aFrom, aÒî);

		mFrom = aFrom;
		mTo = aÒî;
	}

	public int size() {
		return mTo - mFrom + 1;
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean contains(final Object aO) {
		if (!(aO instanceof Number)) {
			return false;
		}

		final int value = ((Number) aO).intValue();
		return (value >= mFrom) && (value <= mTo);
	}

	public Integer[] toArray() {
		final Integer retval[] = new Integer[size()];
		copy(retval);

		return retval;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(final T[] aArray) {
		final int sz = size();

		final T[] retval;
		if (aArray.length >= size()) {
			retval = aArray;
		} else {
			retval = (T[]) Array.newInstance(aArray.getClass().getComponentType(), sz);
		}

		copy(retval);
		if (aArray.length > sz) {
			aArray[sz] = null;
		}

		return retval;
	}

	private void copy(final Object[] aArray) {
		final int sz = size();

		for (int i = 0; i < sz; ++i) {
			aArray[i] = Integer.valueOf(mFrom + i);
		}
	}

	public Integer get(final int aIndex) {
		checkIndexNoSize(aIndex);
		return Integer.valueOf(aIndex + mFrom);
	}

	@Override
	public int indexOf(final Object aO) {
		return contains(aO) ? (((Number) aO).intValue() - mFrom) : -1;
	}

	public int lastIndexOf(final Object aO) {
		return indexOf(aO);
	}

	public ListIterator<Integer> listIterator(final int aIndex) {
		checkIndexSize(aIndex);
		return new RangeIterator(mFrom, mTo, aIndex);
	}

	public List<Integer> subList(final int aFromIndex, final int aToIndex) {
		checkIndexSize(aFromIndex);
		checkIndexSize(aToIndex);

		if (aFromIndex == aToIndex) {
			return Collections.emptyList();
		}

		checkRange(aFromIndex, aToIndex);

		return new RangeList(mFrom + aFromIndex, mFrom + aToIndex - 1);
	}

	public String toString() {
		return "[" + mFrom + " .. " + ((mTo == (Integer.MAX_VALUE - 1)) ? "" : Integer.valueOf(mTo)) + "]";
	}

	private static class RangeIterator extends AbstractListIterator<Integer> {
		private int mCurrent;
		private final int mBeg;
		private final int mEnd;

		public RangeIterator(final int aBeg, final int aEnd, final int aIdx) {
			mBeg = aBeg;
			mEnd = aEnd;
			mCurrent = aBeg + aIdx;
		}

		public boolean hasNext() {
			return mCurrent <= mEnd;
		}

		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return Integer.valueOf(mCurrent++);
		}

		public Integer previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			return Integer.valueOf(--mCurrent);
		}

		public int nextIndex() {
			return mCurrent - mBeg;
		}
	}
}
