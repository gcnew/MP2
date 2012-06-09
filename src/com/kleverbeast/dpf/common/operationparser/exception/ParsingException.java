package com.kleverbeast.dpf.common.operationparser.exception;

public class ParsingException extends Exception {
	public ParsingException() {
		super();
	}

	public ParsingException(final String aMessage, final Throwable aCause) {
		super(aMessage, aCause);
	}

	public ParsingException(final String aMessage) {
		super(aMessage);
	}

	public ParsingException(final Throwable aCause) {
		super(aCause);
	}
}
