package re.agiledesign.mp2.exception;

public class SourceException extends ParsingException {
	public SourceException(final String aMessage) {
		super(aMessage);
	}

	public SourceException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}
}
