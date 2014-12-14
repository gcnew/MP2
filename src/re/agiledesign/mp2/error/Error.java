package re.agiledesign.mp2.error;

public abstract class Error {
	public enum Severity {
		NOTE, WARNING, ERROR,
	}

	private final Severity mSeverity;
	private final String mMessage;

	public Error(final String aMessage, final Severity aSeverity) {
		mMessage = aMessage;
		mSeverity = aSeverity;
	}

	public Severity getSeverity() {
		return mSeverity;
	}

	public String getMessage() {
		return mMessage;
	}
}
