package com.kleverbeast.dpf.common.operationparser.collection;

public class ConsListWrapper<T> extends AbstractConsList<T> {
	private final ImmutableList<T> mList;

	public ConsListWrapper(final ImmutableList<T> aList) {
		mList = aList;
	}

	public T first() {
		return mList.get(0);
	}

	public AbstractConsList<T> rest() {
		final ImmutableList<T> retval = mList.subList2(1, -1);

		return (retval != null) ? new ConsListWrapper<T>(retval) : null;
	}
}
