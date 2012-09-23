package com.kleverbeast.dpf.common.operationparser.collection;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class RangeList implements List<Integer> {
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

	public Iterator<Integer> iterator() {
		return listIterator();
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

	public boolean add(final Integer aE) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(final Object aO) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(final Collection<?> aC) {
		for (final Object o : aC) {
			if (!contains(o)) {
				return false;
			}
		}

		return true;
	}

	public boolean addAll(final Collection<? extends Integer> aC) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(final int aIndex, final Collection<? extends Integer> aC) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(final Collection<?> aC) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(final Collection<?> aC) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Integer get(final int aIndex) {
		checkIndexNoSize(aIndex);
		return aIndex + mFrom;
	}

	public Integer set(final int aIndex, final Integer aElement) {
		throw new UnsupportedOperationException();
	}

	public void add(final int aIndex, final Integer aElement) {
		throw new UnsupportedOperationException();
	}

	public Integer remove(final int aIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(final Object aO) {
		return contains(aO) ? (((Number) aO).intValue() - mFrom) : -1;
	}

	public int lastIndexOf(final Object aO) {
		return indexOf(aO);
	}

	public ListIterator<Integer> listIterator() {
		return listIterator(0);
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

	private void checkIndexNoSize(final int aIndex) {
		if (aIndex < 0) {
			throw new IndexOutOfBoundsException("Negative index passed: " + aIndex);
		}

		final int sz = size();
		if (aIndex >= sz) {
			throw new IndexOutOfBoundsException("Size: " + sz + "; index passed: " + aIndex);
		}
	}

	private void checkIndexSize(final int aIndex) {
		if (aIndex < 0) {
			throw new IndexOutOfBoundsException("Negative index passed: " + aIndex);
		}

		final int sz = size();
		if (aIndex > sz) {
			throw new IndexOutOfBoundsException("Size: " + sz + "; index passed: " + aIndex);
		}
	}

	private void checkRange(final int aFromIndex, final int aToIndex) {
		if (aFromIndex > aToIndex) {
			throw new IndexOutOfBoundsException("From index less than To index: " + aFromIndex + " -> " + aToIndex);
		}
	}

	/**
	 * Copied from {@link AbstractList}
	 */
	public boolean equals(final Object aObject) {
		if (aObject == this) {
			return true;
		}
		if (!(aObject instanceof List)) {
			return false;
		}

		final ListIterator<?> e1 = listIterator();
		final ListIterator<?> e2 = ((List<?>) aObject).listIterator();
		while (e1.hasNext() && e2.hasNext()) {
			final Object o1 = e1.next();
			final Object o2 = e2.next();
			if (!(o1 == null ? o2 == null : o1.equals(o2))) {
				return false;
			}
		}

		return !(e1.hasNext() || e2.hasNext());
	}

	/**
	 * Copied from {@link AbstractList}
	 */
	public int hashCode() {
		int hashCode = 1;
		final Iterator<?> i = iterator();

		while (i.hasNext()) {
			final Object obj = i.next();
			hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}

		return hashCode;
	}

	public String toString() {
		return "[" + mFrom + " .. " + ((mTo == (Integer.MAX_VALUE - 1)) ? "" : mTo) + "]";
	}

	private static class RangeIterator implements ListIterator<Integer> {
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

			return mCurrent++;
		}

		public boolean hasPrevious() {
			return mCurrent > mBeg;
		}

		public Integer previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			return --mCurrent;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public int nextIndex() {
			return mCurrent - mBeg;
		}

		public int previousIndex() {
			return nextIndex() - 1;
		}

		public void set(final Integer aE) {
			throw new UnsupportedOperationException();
		}

		public void add(final Integer aE) {
			throw new UnsupportedOperationException();
		}
	}
}
