package re.agiledesign.mp2.exception;

import re.agiledesign.mp2.parser.VarInfo;
import re.agiledesign.mp2.util.StringUtil;

public class VariableNotClosable extends ParsingException {
	public VariableNotClosable(final VarInfo aVariable) {
		super(StringUtil.format("Variable '{}' declared as {} cannot be captured",
				aVariable.getVarName(),
				aVariable.getVisibility()));
	}
}
