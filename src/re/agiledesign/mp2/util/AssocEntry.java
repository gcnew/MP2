package com.kleverbeast.dpf.common.operationparser.util;

public class AssocEntry<T1, T2> {
	private final T1 mKey;
	private final T2 mValue;

	public AssocEntry(final T1 aKey, final T2 aValue) {
		mKey = aKey;
		mValue = aValue;
	}

	public T1 getKey() {
		return mKey;
	}

	public T2 getValue() {
		return mValue;
	}
}
