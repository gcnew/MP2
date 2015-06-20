package re.agiledesign.mp2;

import java.util.Map;

import re.agiledesign.mp2.internal.ControlFlow.Type;
import re.agiledesign.mp2.internal.NameScope;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.statements.Statement;

public class ParsedScript {
	private final int mLocalCount;
	private final Statement mStartingStatement;
	private final Map<String, ? extends Object> mGlobals;

	/* package */ParsedScript(final Statement aStartingStatement, final Map<String, ?> aGlobals, final int aLocalCount) {
		mGlobals = aGlobals;
		mLocalCount = aLocalCount;
		mStartingStatement = aStartingStatement;
	}

	/* package */Interpreter createInterpreter(final Map<String, ?> aGlobalVars) {
		final NameScope scope = new NameScope(null, null, mGlobals, mLocalCount);

		if (aGlobalVars != null) {
			scope.putAll(aGlobalVars);
		}

		return new Interpreter(scope, this);
	}

	/* package */Object execute(final Scope aScope) throws Exception {
		mStartingStatement.execute(aScope);

		if ((aScope.getControlFlow() != null) && (aScope.getControlFlow().getType() == Type.RETURN)) {
			return aScope.getControlFlow().getValue();
		}

		return null;
	}
}
