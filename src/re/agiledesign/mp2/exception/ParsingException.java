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

	public String getErrorMessage() {
		final StringBuilder sb = new StringBuilder();

		sb.append(getMessage());

		Throwable current = this.getCause();
		while (current != null) {
			sb.append('\n').append(current.getMessage());

			// include just one foreign exception
			if (!(current instanceof ParsingException)) {
				break;
			}

			current = current.getCause();
		}

		return sb.toString();
	}
}
