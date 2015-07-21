package re.agiledesign.mp2;

import re.agiledesign.mp2.internal.ControlFlow.Type;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.statements.Statement;

public class ParsedScript {
	private final int mLocalCount;
	private final Statement mStartingStatement;

	public ParsedScript(final Statement aStartingStatement, final int aLocalCount) {
		mLocalCount = aLocalCount;
		mStartingStatement = aStartingStatement;
	}

	public Object execute(final Scope aScope) throws Exception {
		mStartingStatement.execute(aScope);

		if ((aScope.getControlFlow() != null) && (aScope.getControlFlow().getType() == Type.RETURN)) {
			return aScope.getControlFlow().getValue();
		}

		return null;
	}

	public int getLocalCount() {
		return mLocalCount;
	}
}
