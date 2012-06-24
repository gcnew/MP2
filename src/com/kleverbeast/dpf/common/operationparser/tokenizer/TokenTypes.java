package com.kleverbeast.dpf.common.operationparser.tokenizer;

public enum TokenTypes {
	CONSTANT, // null, string, integer or boolean (true, false) constant
	LITERAL, // a literal (function or variable name or legacy variable - \$[a-zA-Z]+)
	OPERATOR, // a functional operator (+, -, =, etc)
	KEYWORD, // a keyword (do, while, for, etc)
	TYPE, // a type (string, int, double, etc)
	GRAMMAR_TOKEN, // one of the token 
}
