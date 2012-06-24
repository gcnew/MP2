package com.kleverbeast.dpf.common.operationparser;

import java.util.Map;

public class InterpreterFactory {
	public static Interpreter createInterpreter(final String aScriptSource, final Map<String, ? extends Object> aArgs)
			throws Exception {
		return createInterpreter(parseScript(aScriptSource), aArgs);
	}

	public static Interpreter createInterpreter(final ParsedScript aScript, final Map<String, ? extends Object> aArgs) {
		return aScript.createInterpreter(aArgs);
	}

	public static ParsedScript parseScript(final String aScriptSource) throws Exception {
		final OperationParser parser = new OperationParser(aScriptSource);

		return parser.parse();
	}
}
