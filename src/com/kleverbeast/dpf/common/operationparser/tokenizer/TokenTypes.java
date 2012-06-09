package com.kleverbeast.dpf.common.operationparser.tokenizer;

public enum TokenTypes {
	CONSTANT, // null, string, integer or boolean (true, false) constant
	LITERAL, // a function name
	VARIABLE, // a variable \$[a-zA-Z]+
	OPERATOR, // a functional operator (+, -, =, etc)
	KEYWORD, // a keyword (do, while, for, etc)
	TYPE, // a type (string, int, double, etc)
	GRAMMAR_TOKEN, // one of the token 
}
