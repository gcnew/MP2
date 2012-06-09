package com.kleverbeast.dpf.common.operationparser.exception;

public class ScriptException extends Exception {
	public ScriptException() {
		super();
	}

	public ScriptException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public ScriptException(String aMessage) {
		super(aMessage);
	}

	public ScriptException(Throwable aCause) {
		super(aCause);
	}
}
