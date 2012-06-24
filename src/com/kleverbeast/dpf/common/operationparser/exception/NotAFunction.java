package com.kleverbeast.dpf.common.operationparser.exception;

public class NotAFunction extends ScriptException {
	public NotAFunction(final String aReferenceName) {
		super("Reference '" + aReferenceName + "' is not a function");
	}
}
