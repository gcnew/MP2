package re.agiledesign.mp2.internal;

public class LocalScope implements Scope {
	private final Context mPrev;

	public LocalScope(final Context aPrev) {
		mPrev = aPrev;
	}

	public Context getPrevious() {
		return mPrev;
	}

	public void setVariable(final String aName, final Object aValue) {
		mPrev.setVariable(aName, aValue);
	}

	public Object getVariable(final String aName) {
		return mPrev.getVariable(aName);
	}

	public Object getLocalVariable(int aIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocalVariable(int aIndex, Object aValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public ControlFlow getControlFlow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setControlFlow(ControlFlow aControlFlow) {
		// TODO Auto-generated method stub

	}
}
