package re.agiledesign.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import re.agiledesign.mp2.exception.ArgumentAlreadyExists;
import re.agiledesign.mp2.exception.VariableAlreadyDeclared;
import re.agiledesign.mp2.util.Util;

public class LexicalScope {
	// private final LexicalScope mPrevious;
	private final List<String> mArgsArray = new ArrayList<String>();
	private final Set<String> mAssigned = new HashSet<String>();
	private final Map<String, Integer> mLocalVars = new HashMap<String, Integer>();

	/* public LexicalScope(final LexicalScope aPrevious) {
		mPrevious = aPrevious;
	} */

	public void addArgument(final String aArgName) throws ArgumentAlreadyExists {
		final String argName = Util.stripVariableName(aArgName);

		if (mArgsArray.contains(argName)) {
			throw new ArgumentAlreadyExists(aArgName);
		}

		mArgsArray.add(argName);
	}

	public int getArgumentIndex(final String aArgName) {
		return mArgsArray.indexOf(Util.stripVariableName(aArgName));
	}

	public List<String> getArgumentsArray() {
		return mArgsArray;
	}

	public int getLocalsCount() {
		return mLocalVars.size();
	}

	public void addLocalVariable(final String aVarName) throws VariableAlreadyDeclared {
		final String varName = Util.stripVariableName(aVarName);

		if (mLocalVars.containsKey(varName) || mArgsArray.contains(varName)) {
			throw new VariableAlreadyDeclared(aVarName);
		}

		mLocalVars.put(varName, Integer.valueOf(mLocalVars.size()));
	}

	public int getLocalVariableIndex(final String aVarName) {
		final String varName = Util.stripVariableName(aVarName);
		Integer retval = mLocalVars.get(varName);

		// old style variables were declared on first use, add it if not present
		if ((retval == null) && Util.isLegacyVariable(aVarName)) {
			retval = Integer.valueOf(mLocalVars.size());

			mLocalVars.put(varName, retval);
		}

		return (retval != null) ? retval.intValue() : (-1);
	}

	public void setAssigned(final String aVarName) {
		mAssigned.add(Util.stripVariableName(aVarName));
	}

	public boolean isAssigned(final String aVarName) {
		return mAssigned.contains(Util.stripVariableName(aVarName));
	}
}
