package re.agiledesign.mp2.error;

import re.agiledesign.mp2.exception.ParsingException;

public class CompilationError extends Error {
	public CompilationError(final ParsingException aException) {
		this(aException.getErrorMessage());
	}

	public CompilationError(final String aMessage) {
		super(aMessage, Severity.ERROR);
	}
}
