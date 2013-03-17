package re.agiledesign.mp2.exception;

public class UnimplementedOperatorException extends ParsingException {
	public UnimplementedOperatorException() {
		super();
	}

	public UnimplementedOperatorException(final String aMessage, final Throwable aCause) {
		super(aMessage, aCause);
	}

	public UnimplementedOperatorException(final String aMessage) {
		super(aMessage);
	}

	public UnimplementedOperatorException(final Throwable aCause) {
		super(aCause);
	}
}
