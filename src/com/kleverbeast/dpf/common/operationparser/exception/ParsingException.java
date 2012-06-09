package com.kleverbeast.dpf.common.operationparser.exception;

public class ParsingException extends Exception {
    public ParsingException() {
        super();
    }

    public ParsingException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }

    public ParsingException(String aMessage) {
        super(aMessage);
    }

    public ParsingException(Throwable aCause) {
        super(aCause);
    }
}
