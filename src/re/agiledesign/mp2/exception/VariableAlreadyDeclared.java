package re.agiledesign.mp2.exception;

import re.agiledesign.mp2.parser.VarInfo;
import re.agiledesign.mp2.util.StringUtil;

public class VariableAlreadyDeclared extends ParsingException {
	public VariableAlreadyDeclared(final VarInfo aVariable) {
		super(StringUtil.format("Variable '{}' has already been declared as {}",
				aVariable.getVarName(),
				aVariable.getVisibility()));
	}
}
