package re.agiledesign.mp2;

import java.util.Map;

import re.agiledesign.mp2.internal.Context;
import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.sourceprovider.SourceProvider;

public class Interpreter {
	private final Context mContext;

	/* package */Interpreter(final SourceProvider aProvider, final Map<String, ?> aGlobals) {
		if (aGlobals == null) {
			mContext = new Context(aProvider);
		} else {
			mContext = new Context(aProvider, aGlobals);
		}
	}

	// Debugger getDebugger();

	public Object eval(final String aSource) throws Exception {
		return eval(InterpreterFactory.parseScript(aSource));
	}

	public Object eval(final ParsedScript aScript) throws Exception {
		final Scope scope = new FunctionScope(mContext, null, aScript.getLocalCount());

		return aScript.execute(scope);
	}

	public Object getGlobal(final String aName) {
		return mContext.getVariable(aName);
	}

	public void setGlobal(final String aName, final Object aValue) {
		mContext.setVariable(aName, aValue);
	}

	public Map<String, Object> getGlobals() {
		return mContext.getVariables();
	}

	public void addGlobals(final Map<String, ?> aGlobals) {
		mContext.addVariables(aGlobals);
	}
}
