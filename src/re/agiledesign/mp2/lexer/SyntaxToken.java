package re.agiledesign.mp2.lexer;

public enum SyntaxToken {
	SCOPE, // the . operator
	COMMA, // the comma operator ','
	COLON, // the : operator
	QUEST, // the ? operator
	RANGE, // the .. operator
	ARROW, // the -> operator
	LAMBDA, // the => operator
	SEMICOL, // end of statement ';'
	O_BRACK, // opening bracket '('
	C_BRACK, // closing bracket ')'
	O_INDEX, // opening index bracket '['
	C_INDEX, // closing index bracket ']'
	O_BLOCK, // opening block bracket '{'
	C_BLOCK; // closing block bracket '}'
}
