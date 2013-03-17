package com.kleverbeast.dpf.common.operationparser.internal;

public class FunctionScope implements Scope {
	private final NameScope mPrev;
	private final Object[] mArgs;
	private final Object[] mLocalVars;
	private ControlFlow mControlFlow = null;

	public FunctionScope(final NameScope aPrev, final Object[] aArgs) {
		this(aPrev, aArgs, 0);
	}

	public FunctionScope(final NameScope aPrev, final Object[] aArgs, final int aLocalVarsCount) {
		mPrev = aPrev;
		mArgs = aArgs;
		mLocalVars = new Object[aLocalVarsCount];
	}

	public NameScope getPrevious() {
		return mPrev;
	}

	public void setVariable(final String aName, final Object aValue) {
		mPrev.setVariable(aName, aValue);
	}

	public Object getVariable(final String aName) {
		return mPrev.getVariable(aName);
	}

	public Object getLocalVariable(final int aIndex) {
		return mLocalVars[aIndex];
	}

	public void setLocalVariable(final int aIndex, final Object aValue) {
		mLocalVars[aIndex] = aValue;
	}

	public ControlFlow getControlFlow() {
		return mControlFlow;
	}

	public void setControlFlow(final ControlFlow aControlFlow) {
		mControlFlow = aControlFlow;
	}

	public Object[] getArguments() {
		return mArgs;
	}

	public Object getArgument(final int aIndex) {
		return mArgs[aIndex];
	}

	public void setArgument(final int aIndex, final Object aValue) {
		mArgs[aIndex] = aValue;
	}
}
