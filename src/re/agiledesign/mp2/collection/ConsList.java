package com.kleverbeast.dpf.common.operationparser.collection;

public class ConsList<T> extends AbstractConsList<T> {
	private final T mFirst;
	private final AbstractConsList<T> mRest;

	public ConsList(final T aValue) {
		this(aValue, null);
	}

	public ConsList(final T aValue, final AbstractConsList<T> aRest) {
		mFirst = aValue;
		mRest = aRest;
	}

	public final T first() {
		return mFirst;
	}

	public final AbstractConsList<T> rest() {
		return mRest;
	}
}
