package com.kleverbeast.dpf.common.operationparser.exception;

public class VariableAlreadyDeclared extends ParsingException {
	public VariableAlreadyDeclared(String aArgName) {
		super("Local variable with name '" + aArgName + "' has already been declared");
	}
}
