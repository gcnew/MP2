package re.agiledesign.mp2.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.exception.VariableAlreadyDeclared;
import re.agiledesign.mp2.exception.VariableNotClosable;
import re.agiledesign.mp2.parser.VarInfo.Visibility;
import re.agiledesign.mp2.util.AssertUtil;

public class LexicalScope {
	private final LexicalScope mParentScope;
	private final int mIndices[] = new int[Visibility.values().length];
	private final Map<String, VarInfo> mVariables = new HashMap<String, VarInfo>();

	private int mUniqueId;
	private int mLoopNesting;

	private static VarInfo DUMMY_VAR = new VarInfo(null, null, null, -1);

	public static class CaptureMapping {
		public final int localIndex;
		public final int captureIndex;
		public final Visibility visibility;

		public CaptureMapping(final VarInfo aVariable) {
			localIndex = aVariable.getIndex();
			captureIndex = aVariable.getCaptured().getIndex();
			visibility = aVariable.getCaptured().getVisibility();
		}
	}

	public LexicalScope(final LexicalScope aParentScope) {
		mParentScope = aParentScope;
	}

	public VarInfo getVariable(final String aVarName) throws ParsingException {
		final VarInfo retval = getVar(aVarName, null);
		return (retval != DUMMY_VAR) ? retval : null;
	}

	public VarInfo getVariable(final String aVarName, final Visibility aVisibility) throws ParsingException {
		final VarInfo retval = getVar(aVarName, aVisibility);
		return (retval != DUMMY_VAR) ? retval : null;
	}

	private VarInfo getVar(final String aVarName, final Visibility aVisibility) throws ParsingException {
		final VarInfo var = mVariables.get(aVarName);

		if (var == null) {
			if (mParentScope == null) {
				return DUMMY_VAR;
			}

			final VarInfo closed = mParentScope.getVar(aVarName, aVisibility);
			if (closed == DUMMY_VAR) {
				return DUMMY_VAR;
			}

			// globals should not be captured
			// TODO: revise this if "updatable" captures are implemented
			if (closed.isGlobal()) {
				return DUMMY_VAR;
			}

			if (!(closed.isVar() || closed.isArgument() || closed.isCapture())) {
				throw new VariableNotClosable(closed);
			}

			return addVariable0(aVarName, Visibility.CAPTURE, closed);
		}

		if (aVisibility != null) {
			AssertUtil.runtimeAssert(var.getVisibility() == aVisibility, "Variable not of expected visibility!");
		}

		return var;
	}

	private int getNextIndex(final Visibility aVisibility) {
		return mIndices[aVisibility.ordinal()]++;
	}

	public VarInfo getUniqueVariable() {
		final String varName = "!tmp" + mUniqueId++;
		final VarInfo retval = new VarInfo(varName, Visibility.LOCAL, this, getNextIndex(Visibility.LOCAL));

		mVariables.put(varName, retval);

		return retval;
	}

	public VarInfo addVariable(final String aVarName, final Visibility aVisibility) throws ParsingException {
		final VarInfo existingVar = getVariable(aVarName);
		if (getVariable(aVarName) != null) {
			throw new VariableAlreadyDeclared(existingVar);
		}

		return addVariable0(aVarName, aVisibility, null);
	}

	private VarInfo addVariable0(final String aVarName, final Visibility aVisibility, final VarInfo aCaptured) {
		final Visibility store = aVisibility.getStore();
		if (store != aVisibility) {
			getNextIndex(aVisibility);
		}

		final VarInfo retval = new VarInfo(aVarName, aVisibility, this, getNextIndex(store), aCaptured);
		if (aVisibility == Visibility.ARGUMENT || aVisibility == Visibility.GLOBAL || aVisibility == Visibility.CAPTURE) {
			retval.assign();
		}

		mVariables.put(aVarName, retval);

		return retval;
	}

	public int getCountOf(final Visibility aVisibility) {
		return mIndices[aVisibility.ordinal()];
	}

	public int getIndexOf(final String aVarName) throws ParsingException {
		return getVar(aVarName, null).getIndex();
	}

	public Visibility getVisibilityOf(final String aVarName) throws ParsingException {
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

	public List<CaptureMapping> getCaptureMappings() {
		final List<CaptureMapping> mappings = new ArrayList<CaptureMapping>();

		for (final VarInfo v : mVariables.values()) {
			if (v.isCapture()) {
				mappings.add(new CaptureMapping(v));
			}
		}

		return mappings;
	}

	public LexicalScope getParentScope() {
		return mParentScope;
	}

	public void enterLoop() {
		++mLoopNesting;
	}

	public void leaveLoop() {
		AssertUtil.runtimeAssert(isInLoop());
		--mLoopNesting;
	}

	public boolean isInLoop() {
		return mLoopNesting > 0;
	}
}
