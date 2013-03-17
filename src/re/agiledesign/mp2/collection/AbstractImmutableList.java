package com.kleverbeast.dpf.common.operationparser.collection;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractImmutableList<T> implements ImmutableList<T> {
	protected void checkIndexNoSize(final int aIndex) {
		if (aIndex < 0) {
			throw new IndexOutOfBoundsException("Negative index passed: " + aIndex);
		}

		final int sz = size();
		if (aIndex >= sz) {
			throw new IndexOutOfBoundsException("Size: " + sz + "; index passed: " + aIndex);
		}
	}

	protected void checkIndexSize(final int aIndex) {
		if (aIndex < 0) {
			throw new IndexOutOfBoundsException("Negative index passed: " + aIndex);
		}

		final int sz = size();
		if (aIndex > sz) {
			throw new IndexOutOfBoundsException("Size: " + sz + "; index passed: " + aIndex);
		}
	}

	protected void checkRange(final int aFromIndex, final int aToIndex) {
		if (aFromIndex > aToIndex) {
			throw new IndexOutOfBoundsException("From index less than To index: " + aFromIndex + " -> " + aToIndex);
		}
	}

	public boolean add(final T aE) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(final Object aO) {
		throw new UnsupportedOperationException();
	}

	public Iterator<T> iterator() {
		return listIterator();
	}

	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	public boolean containsAll(final Collection<?> aC) {
		for (final Object o : aC) {
			if (!contains(o)) {
				return false;
			}
		}

		return true;
	}

	public ImmutableList<T> subList2(final int aFrom, final int aTo) {
		final List<T> retval = subList(aFrom, (aTo < 0) ? size() : aTo);

		if ((retval == null) || retval.isEmpty()) {
			return null;
		}

		return (ImmutableList<T>) retval;
	}

	public boolean addAll(final Collection<? extends T> aC) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(final int aIndex, final Collection<? extends T> aC) {
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

	public T set(final int aIndex, final T aElement) {
		throw new UnsupportedOperationException();
	}

	public void add(final int aIndex, final T aElement) {
		throw new UnsupportedOperationException();
	}

	public T remove(final int aIndex) {
		throw new UnsupportedOperationException();
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

	protected static boolean equals(final Object aO1, final Object aO2) {
		return (aO1 == aO2) || ((aO1 != null) && aO1.equals(aO2));
	}

	protected static abstract class AbstractListIterator<T> implements ListIterator<T> {
		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void set(final T aE) {
			throw new UnsupportedOperationException();
		}

		public void add(final T aE) {
			throw new UnsupportedOperationException();
		}

		public boolean hasPrevious() {
			return nextIndex() != 0;
		}

		public int previousIndex() {
			return nextIndex() - 1;
		}

	}
}
