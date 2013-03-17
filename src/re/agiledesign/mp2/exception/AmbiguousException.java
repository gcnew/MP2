package re.agiledesign.mp2.exception;

public class AmbiguousException extends ScriptException {
	public AmbiguousException(final String aMessage, final String aExplanation) {
		super(aMessage + "\n >> " + aExplanation.replace("\n", "\n >> "));
	}
}
