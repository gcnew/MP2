package com.kleverbeast.dpf.common.operationparser.internal;

public class ControlFlow {
	public static enum Type {
		RETURN, // return
		BREAK, // break 
		CONTINUE; // continue
	};

	public static final ControlFlow BREAK = new ControlFlow(Type.BREAK, null);
	public static final ControlFlow CONTINUE = new ControlFlow(Type.CONTINUE, null);

	private final Type mType;
	private final Object mValue;

	private ControlFlow(final Type aType, final Object aValue) {
		mType = aType;
		mValue = aValue;
	}

	public static ControlFlow getReturn(final Object aValue) {
		return new ControlFlow(Type.RETURN, aValue);
	}

	public Type getType() {
		return mType;
	}

	public Object getValue() {
		return mValue;
	}
}
