package re.agiledesign.mp2.lexer;

public enum TokenType {
	CONSTANT, // null, string/float/integral literal, boolean constant (true, false)
	IDENTIFIER, // an identifier (function or variable name or legacy variable - \$[_a-zA-Z][_a-zA-Z0-9]*)
	OPERATOR, // a functional operator (+, -, =, etc)
	KEYWORD, // a keyword (do, while, for, etc)
	TYPE, // a type (string, int, double, etc)
	SYNTAX_TOKEN, // one of the syntactic/punctuation tokens ('.', ',', ':', etc) 
}
