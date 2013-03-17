package re.agiledesign.mp2.exception;

public class NotAFunction extends ScriptException {
	public NotAFunction(final String aReferenceName) {
		super("Reference '" + aReferenceName + "' is not a function");
	}
}
