package com.kleverbeast.dpf.common.operationparser.exception;

public class UnimplementedOperatorException extends ParsingException {
	public UnimplementedOperatorException() {
		super();
	}

	public UnimplementedOperatorException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public UnimplementedOperatorException(String aMessage) {
		super(aMessage);
	}

	public UnimplementedOperatorException(Throwable aCause) {
		super(aCause);
	}
}
