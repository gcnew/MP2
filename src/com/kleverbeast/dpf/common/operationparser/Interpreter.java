package com.kleverbeast.dpf.common.operationparser;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

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
}
