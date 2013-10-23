package re.agiledesign.mp2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import re.agiledesign.mp2.VarInfo.Visibility;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.exception.VariableAlreadyDeclared;
import re.agiledesign.mp2.util.AssertUtil;
import re.agiledesign.mp2.util.Util;

public class LexicalScope {
	private final LexicalScope mParentScope;
	private final int mIndices[] = new int[Visibility.values().length];
	private final Map<String, VarInfo> mVariables = new HashMap<String, VarInfo>();

	private static VarInfo DUMMY_VAR = new VarInfo(null, null, null, -1);

	public LexicalScope(final LexicalScope aParentScope) {
		mParentScope = aParentScope;
	}

	public VarInfo getVariable(final String aVarName) {
		final VarInfo retval = getVar(aVarName, null);
		return (retval != DUMMY_VAR) ? retval : null;
	}

	public VarInfo getVariable(final String aVarName, final Visibility aVisibility) {
		final VarInfo retval = getVar(aVarName, aVisibility);
		return (retval != DUMMY_VAR) ? retval : null;
	}

	private VarInfo getVar(final String aVarName, final Visibility aVisibility) {
		final VarInfo var = mVariables.get(Util.stripVariableName(aVarName));

		if (var == null) {
			return DUMMY_VAR;
		}

		if (aVisibility != null) {
			AssertUtil.runtimeAssert(var.getVisibility() == aVisibility, "Variable not of expected visibility!");
		}

		return var;
	}

	private int getNextIndex(final Visibility aVisibility) {
		return mIndices[aVisibility.ordinal()]++;
	}

	public VarInfo addVariable(final String aVarName, final Visibility aVisibility) throws ParsingException {
		final String varName = Util.stripVariableName(aVarName);

		final VarInfo existingVar = mVariables.get(varName);
		if (existingVar != null) {
			throw new VariableAlreadyDeclared(existingVar);
		}

		final VarInfo retval = new VarInfo(varName, aVisibility, this, getNextIndex(aVisibility));
		mVariables.put(varName, retval);

		return retval;
	}

	public int getCountOf(final Visibility aVisibility) {
		return mIndices[aVisibility.ordinal()];
	}

	public int getIndexOf(final String aVarName) {
		return getVar(aVarName, null).getIndex();
	}

	public Visibility getVisibilityOf(final String aVarName) {
		return getVar(aVarName, null).getVisibility();
	}

	public List<String> getArgumentsArray() {
		final String argumentNames[] = new String[getCountOf(Visibility.ARGUMENT)];

		for (final Entry<String, VarInfo> e : mVariables.entrySet()) {
			if (e.getValue().isArgument()) {
				argumentNames[e.getValue().getIndex()] = e.getKey();
			}
		}

		return Arrays.asList(argumentNames);
	}

	public LexicalScope getParentScope() {
		return mParentScope;
	}
}
