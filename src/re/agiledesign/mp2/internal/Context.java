package re.agiledesign.mp2.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.InterpreterFactory;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.sourceprovider.SourceProvider;
import re.agiledesign.mp2.util.AssertUtil;

public class Context implements Scope {
	private ControlFlow mControlFlow = null;

	private final Map<String, Object> mNamedVars;

	private final SourceProvider mProvider;
	private final Map<String, Context> mModuleCache = new HashMap<String, Context>();

	public Context(final SourceProvider aProvider) {
		mProvider = aProvider;
		mNamedVars = new HashMap<String, Object>();
	}

	public Context(final SourceProvider aProvider, final Map<String, ?> aNamedVars) {
		mProvider = aProvider;
		mNamedVars = new HashMap<String, Object>(aNamedVars);
	}

	public Context getPrevious() {
		return null;
	}

	public void setVariable(final String aName, final Object aValue) {
		mNamedVars.put(aName, aValue);
	}

	public Object getVariable(final String aName) {
		if (mNamedVars.containsKey(aName)) {
			return mNamedVars.get(aName);
		}

		return null;
	}

	public Map<String, Object> getVariables() {
		return Collections.unmodifiableMap(mNamedVars);
	}

	public void addVariables(final Map<String, ?> aVariables) {
		mNamedVars.putAll(aVariables);
	}

	public Object getLocalVariable(final int aIndex) {
		AssertUtil.never();

		return null;
	}

	public void setLocalVariable(final int aIndex, final Object aValue) {
		AssertUtil.never();
	}

	public ControlFlow getControlFlow() {
		return mControlFlow;
	}

	public void setControlFlow(final ControlFlow aControlFlow) {
		mControlFlow = aControlFlow;
	}

	public void loadModule(final String aPath) throws ParsingException, Exception {
		final Context scope = loadModule0(aPath);

		mNamedVars.putAll(scope.getVariables());
	}

	private Context loadModule0(final String aPath) throws ParsingException, Exception {
		final String resolvedPath = mProvider.resolve(aPath);

		if (mModuleCache.containsKey(resolvedPath)) {
			return mModuleCache.get(resolvedPath);
		}

		final String source = mProvider.getSource(resolvedPath);

		final Context context = new Context(mProvider);
		mModuleCache.put(resolvedPath, context);

		InterpreterFactory.parseScript(source).execute(context);

		return context;
	}
}
