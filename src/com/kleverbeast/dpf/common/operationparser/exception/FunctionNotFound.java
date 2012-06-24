package com.kleverbeast.dpf.common.operationparser.exception;

public class FunctionNotFound extends ScriptException {
	public FunctionNotFound(final String aFunctionName) {
		super("Function '" + aFunctionName + "' not found");
	}
}
