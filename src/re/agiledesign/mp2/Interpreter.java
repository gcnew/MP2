package re.agiledesign.mp2;

import re.agiledesign.mp2.internal.Scope;

public class Interpreter {
	private final Scope mScope;
	private final ParsedScript mScript;

	/* package */Interpreter(final Scope aScope, final ParsedScript aScript) {
		mScope = aScope;
		mScript = aScript;
	}

	public Object eval() throws Exception {
		return mScript.execute(mScope);
	}

	public Object getGlobal(final String aName) {
		return mScope.getVariable(aName);
	}

	public void setGlobal(final String aName, final Object aValue) {
		mScope.setVariable(aName, aValue);
	}
}
