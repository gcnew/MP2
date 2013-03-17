package com.kleverbeast.dpf.common.operationparser.util;

import java.util.Iterator;

public class Node<T> implements Iterable<T> {
	private final T mValue;
	private final Node<T> mNext;

	public Node(final T aValue, final Node<T> aNext) {
		mValue = aValue;
		mNext = aNext;
	}

	public T getValue() {
		return mValue;
	}

	public Node<T> getNext() {
		return mNext;
	}

	public boolean hasNext() {
		return mNext != null;
	}

	public Iterator<T> iterator() {
		return new NodeIterator<T>(this);
	}

	private static class NodeIterator<T> implements Iterator<T> {
		private Node<T> mNode;

		public NodeIterator(final Node<T> aNode) {
			mNode = aNode;
		}

		public boolean hasNext() {
			return mNode != null;
		}

		public T next() {
			final T retval = mNode.mValue;
			mNode = mNode.mNext;

			return retval;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
