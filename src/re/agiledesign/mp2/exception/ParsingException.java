package re.agiledesign.mp2.exception;

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
