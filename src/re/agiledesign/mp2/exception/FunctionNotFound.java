package re.agiledesign.mp2.exception;

public class FunctionNotFound extends ScriptException {
	public FunctionNotFound(final String aFunctionName) {
		super("Function '" + aFunctionName + "' not found");
	}
}
