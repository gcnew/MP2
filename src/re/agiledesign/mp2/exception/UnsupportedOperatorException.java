package com.kleverbeast.dpf.common.operationparser.exception;

import static com.kleverbeast.dpf.common.operationparser.util.Util.getClassString;

import com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType;

public class UnsupportedOperatorException extends ScriptException {
	public UnsupportedOperatorException(final OperatorType aOperator, final Object aLeft, final Object aRight) {
		super("Operator " + aOperator + " not applicable for types(" + getClassString(aLeft) + ", "
				+ getClassString(aRight) + ")");
	}
}
