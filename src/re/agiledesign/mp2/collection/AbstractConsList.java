package re.agiledesign.mp2.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import re.agiledesign.mp2.util.StacklessException;

public abstract class AbstractConsList<T> extends AbstractImmutableList<T> implements SequentialList<T> {
	private static final StacklessException SAME_MARK = new StacklessException();

	public int size() {
		return (rest() == null) ? 1 : (rest().size() + 1);
	}

	public boolean isEmpty() {
		return false;
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

		SequentialList<T> l = this;
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

		SequentialList<T> curr = this;
		for (int idx = aIndex; idx != 0; --idx) {
			curr = curr.rest();
			if (curr == null) {
				throw indexGtSizeEx(aIndex);
			}
		}

		return curr.first();
	}

	private IndexOutOfBoundsException indexGtSizeEx(final int aIndex) {
		return new IndexOutOfBoundsException("Index: " + aIndex + " greater than list size");
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
		final SequentialList<T> list = (aIndex == 0) ? this : subList0(aIndex, -1);

		return new ConsListIterator<T>(list);
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

		return subList0(aFromIndex, aToIndex);
	}

	public SequentialList<T> subList2(final int aFromIndex, final int aToIndex) {
		if (aFromIndex < 0) {
			throw new IndexOutOfBoundsException("Negative From index passed");
		}

		if (aFromIndex == aToIndex) {
			return null;
		}

		if ((aToIndex >= 0) && (aFromIndex > aToIndex)) {
			throw new IndexOutOfBoundsException("From index less than To index: " + aFromIndex + " -> " + aToIndex);
		}

		return subList0(aFromIndex, aToIndex);
	}

	private SequentialList<T> subList0(final int aFromIndex, final int aToIndex) {
		if (aToIndex < 0) {
			SequentialList<T> retval = this;

			for (int idx = aFromIndex; idx != 0; --idx) {
				if (retval == null) {
					throw indexGtSizeEx(aFromIndex);
				}

				retval = retval.rest();
			}

			return retval;
		}

		SequentialList<T> curr = this;
		for (int idx = aFromIndex; idx != 0; --idx) {
			if (curr == null) {
				throw new IllegalStateException("From index greater than list length");
			}

			curr = curr.rest();
		}

		try {
			return copy(curr, aToIndex - aFromIndex);
		} catch (final StacklessException ignored) {
			return curr;
		}
	}

	private static <T> AbstractConsList<T> copy(final SequentialList<T> aList, final int aN) throws StacklessException {
		if (aN == 0) {
			if (aList == null) {
				throw SAME_MARK;
			}

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

	private static class ConsListIterator<T> extends AbstractListIterator<T> {
		private int mIndex;
		private SequentialList<T> mList;
		private SequentialList<T> mCurrent;

		public ConsListIterator(final SequentialList<T> aList) {
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
			mCurrent = mList.subList2(mIndex, -1);
			return mCurrent.first();
		}

		public int nextIndex() {
			return mIndex;
		}
	}
}
