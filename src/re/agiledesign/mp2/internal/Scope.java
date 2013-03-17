package re.agiledesign.mp2.internal;

public interface Scope {
	public NameScope getPrevious();

	public void setVariable(final String aName, final Object aValue);

	public Object getVariable(final String aName);

	public Object getLocalVariable(final int aIndex);

	public void setLocalVariable(final int aIndex, final Object aValue);

	public ControlFlow getControlFlow();

	public void setControlFlow(final ControlFlow aControlFlow);
}
