package re.agiledesign.mp2.internal;

import java.util.HashMap;
import java.util.Map;

public class NameScope implements Scope {
	private final NameScope mPrev;
	private final Object[] mLocalVars;
	private ControlFlow mControlFlow = null;
	private final Map<String, Object> mNamedVars;

	public NameScope(final NameScope aPrev,
			final Object[] aArgs,
			final Map<String, ?> aNamedVars,
			final int aLocalVarsCount) {
		mPrev = aPrev;

		if (aNamedVars != null) {
			mNamedVars = new HashMap<String, Object>(aNamedVars);
		} else {
			mNamedVars = new HashMap<String, Object>();
		}

		mLocalVars = new Object[aLocalVarsCount];
	}

	public void putAll(final Map<String, ?> aLocalVars) {
		mNamedVars.putAll(aLocalVars);
	}

	public NameScope getPrevious() {
		return mPrev;
	}

	public void setVariable(final String aName, final Object aValue) {
		mNamedVars.put(aName, aValue);
	}

	public Object getVariable(final String aName) {
		if (mNamedVars.containsKey(aName)) {
			return mNamedVars.get(aName);
		}

		return (mPrev != null) ? mPrev.getVariable(aName) : null;
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
}
