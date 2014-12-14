package re.agiledesign.mp2.util.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PersistentIterator<T> implements StatefulIterator<T> {
	private T mCurrent;
	private boolean mAtEnd;

	private final Iterator<? extends T> mIterator;

	public PersistentIterator(final Iterator<? extends T> aIterator) {
		mIterator = aIterator;

		advance();
	}

	public T current() {
		if (atEnd()) {
			throw new NoSuchElementException("End of iterator already reached");
		}

		return mCurrent;
	}

	public void advance() {
		if (!mIterator.hasNext()) {
			mAtEnd = true;
			mCurrent = null;
		}

		mCurrent = mIterator.next();
	}

	public boolean atEnd() {
		return mAtEnd;
	}
}
