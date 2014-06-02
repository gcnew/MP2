package re.agiledesign.mp2.util;

public class StacklessException extends Exception {
	public StacklessException() {
		super();
	}

	public StacklessException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public StacklessException(String aMessage) {
		super(aMessage);
	}

	public StacklessException(Throwable aCause) {
		super(aCause);
	}

	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
