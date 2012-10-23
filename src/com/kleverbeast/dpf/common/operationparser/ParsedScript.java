package com.kleverbeast.dpf.common.operationparser;

import java.util.Map;

import com.kleverbeast.dpf.common.operationparser.internal.ControlFlow.Type;
import com.kleverbeast.dpf.common.operationparser.internal.NameScope;
import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;

public class ParsedScript {
	private final Statement mStartingStatement;
	private final Map<String, ? extends Object> mGlobals;

	/* package */ParsedScript(final Statement aStartingStatement, final Map<String, ? extends Object> aGlobals) {
		mGlobals = aGlobals;
		mStartingStatement = aStartingStatement;
	}

	/* package */Interpreter createInterpreter(final Map<String, ? extends Object> aGlobalVars) {
		final NameScope scope = new NameScope(null, null, mGlobals, 0);

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