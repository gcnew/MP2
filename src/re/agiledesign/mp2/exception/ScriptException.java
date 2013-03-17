package re.agiledesign.mp2.exception;

public class ScriptException extends Exception {
	public ScriptException() {
		super();
	}

	public ScriptException(final String aMessage, final Throwable aCause) {
		super(aMessage, aCause);
	}

	public ScriptException(final String aMessage) {
		super(aMessage);
	}

	public ScriptException(final Throwable aCause) {
		super(aCause);
	}
}
