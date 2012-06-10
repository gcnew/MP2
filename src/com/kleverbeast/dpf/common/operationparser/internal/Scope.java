package com.kleverbeast.dpf.common.operationparser.internal;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private final Scope mPrev;
	private final Object[] mArgs;
	private ControlFlow mControlFlow = null;
	private final Map<String, Object> mLocalVars = new HashMap<String, Object>();

	public Scope(final Scope aPrev, final Object[] aArgs) {
		mPrev = aPrev;
		mArgs = aArgs;
	}

	public void setVariable(final String aName, final Object aValue) {
		mLocalVars.put(aName, aValue);
	}

	public Object getVariable(final String aName) {
		if (mLocalVars.containsKey(aName)) {
			return mLocalVars.get(aName);
		}

		return (mPrev != null) ? mPrev.getVariable(aName) : null;
	}

	public ControlFlow getControlFlow() {
		return mControlFlow;
	}

	public void setControlFlow(final ControlFlow aControlFlow) {
		mControlFlow = aControlFlow;
	}

	public Object[] getArgsArray() {
		return mArgs;
	}

	public Object getArg(final int aIndex) {
		return mArgs[aIndex];
	}

	public void setArg(final int aIndex, final Object aValue) {
		mArgs[aIndex] = aValue;
	}
}
