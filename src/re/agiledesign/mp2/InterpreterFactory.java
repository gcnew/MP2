package re.agiledesign.mp2;

import java.util.Map;

public class InterpreterFactory {
	public static Interpreter createInterpreter(final String aScriptSource, final Map<String, ? extends Object> aArgs)
			throws Exception {
		return createInterpreter(parseScript(aScriptSource), aArgs);
	}

	public static Interpreter createInterpreter(final ParsedScript aScript, final Map<String, ? extends Object> aArgs) {
		return aScript.createInterpreter(aArgs);
	}

	public static ParsedScript parseScript(final String aScriptSource) throws Exception {
		final MP2Parser parser = new MP2Parser(aScriptSource);

		return parser.parse();
	}
}
