package re.agiledesign.mp2;

import java.util.Map;

import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.sourceprovider.NullProvider;
import re.agiledesign.mp2.internal.sourceprovider.SourceProvider;
import re.agiledesign.mp2.parser.MP2Parser;

public class InterpreterFactory {
	public static Object eval(final String aSource) throws Exception {
		return getInstance(NullProvider.instance(), null).eval(aSource);
	}

	public static Object eval(final String aSource, final Map<String, ?> aGlobals) throws Exception {
		return getInstance(NullProvider.instance(), aGlobals).eval(aSource);
	}

	public static Interpreter getInstance(final SourceProvider aProvider, final Map<String, ?> aGlobals) {
		return new Interpreter(aProvider, aGlobals);
	}

	public static ParsedScript parseScript(final String aScriptSource) throws ParsingException {
		final MP2Parser parser = new MP2Parser(aScriptSource);

		return parser.parse();
	}

}
