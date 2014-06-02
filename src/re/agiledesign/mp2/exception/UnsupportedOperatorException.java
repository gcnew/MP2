package re.agiledesign.mp2.exception;

import static re.agiledesign.mp2.util.Util.getClassString;
import re.agiledesign.mp2.lexer.OperatorType;

public class UnsupportedOperatorException extends ScriptException {
	public UnsupportedOperatorException(final OperatorType aOperator, final Object aLeft, final Object aRight) {
		super("Operator " + aOperator + " not applicable for types(" + getClassString(aLeft) + ", "
				+ getClassString(aRight) + ")");
	}
}
