package re.agiledesign.mp2.lexer;

import static re.agiledesign.mp2.lexer.SourcePosition.UNKNOWN;
import static re.agiledesign.mp2.lexer.TokenType.CONSTANT;
import static re.agiledesign.mp2.lexer.TokenType.KEYWORD;
import static re.agiledesign.mp2.lexer.TokenType.OPERATOR;
import static re.agiledesign.mp2.lexer.TokenType.SYNTAX_TOKEN;
import static re.agiledesign.mp2.lexer.TokenType.TYPE;
import static re.agiledesign.mp2.util.CoercionUtil.getCoercionType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TokenConstants {
	private static final Token SCOPE = new Token(SYNTAX_TOKEN, SyntaxToken.SCOPE, UNKNOWN);
	private static final Token COMMA = new Token(SYNTAX_TOKEN, SyntaxToken.COMMA, UNKNOWN);
	private static final Token COLON = new Token(SYNTAX_TOKEN, SyntaxToken.COLON, UNKNOWN);
	private static final Token QUEST = new Token(SYNTAX_TOKEN, SyntaxToken.QUEST, UNKNOWN);
	private static final Token RANGE = new Token(SYNTAX_TOKEN, SyntaxToken.RANGE, UNKNOWN);
	private static final Token ARROW = new Token(SYNTAX_TOKEN, SyntaxToken.ARROW, UNKNOWN);
	private static final Token LAMBDA = new Token(SYNTAX_TOKEN, SyntaxToken.LAMBDA, UNKNOWN);
	private static final Token SEMICOL = new Token(SYNTAX_TOKEN, SyntaxToken.SEMICOL, UNKNOWN);
	private static final Token O_BRACK = new Token(SYNTAX_TOKEN, SyntaxToken.O_BRACK, UNKNOWN);
	private static final Token C_BRACK = new Token(SYNTAX_TOKEN, SyntaxToken.C_BRACK, UNKNOWN);
	private static final Token O_INDEX = new Token(SYNTAX_TOKEN, SyntaxToken.O_INDEX, UNKNOWN);
	private static final Token C_INDEX = new Token(SYNTAX_TOKEN, SyntaxToken.C_INDEX, UNKNOWN);
	private static final Token O_BLOCK = new Token(SYNTAX_TOKEN, SyntaxToken.O_BLOCK, UNKNOWN);
	private static final Token C_BLOCK = new Token(SYNTAX_TOKEN, SyntaxToken.C_BLOCK, UNKNOWN);

	private static final Token IF = new Token(KEYWORD, Keyword.IF, UNKNOWN);
	private static final Token ELSE = new Token(KEYWORD, Keyword.ELSE, UNKNOWN);
	private static final Token FOR = new Token(KEYWORD, Keyword.FOR, UNKNOWN);
	private static final Token BREAK = new Token(KEYWORD, Keyword.BREAK, UNKNOWN);
	private static final Token CONTINUE = new Token(KEYWORD, Keyword.CONTINUE, UNKNOWN);
	private static final Token FALLTHROUGH = new Token(KEYWORD, Keyword.FALLTHROUGH, UNKNOWN);
	private static final Token RETURN = new Token(KEYWORD, Keyword.RETURN, UNKNOWN);
	private static final Token DO = new Token(KEYWORD, Keyword.DO, UNKNOWN);
	private static final Token WHILE = new Token(KEYWORD, Keyword.WHILE, UNKNOWN);
	private static final Token SWITCH = new Token(KEYWORD, Keyword.SWITCH, UNKNOWN);
	private static final Token CASE = new Token(KEYWORD, Keyword.CASE, UNKNOWN);
	private static final Token TRY = new Token(KEYWORD, Keyword.TRY, UNKNOWN);
	private static final Token CATCH = new Token(KEYWORD, Keyword.CATCH, UNKNOWN);
	private static final Token FINALLY = new Token(KEYWORD, Keyword.FINALLY, UNKNOWN);
	private static final Token VAR = new Token(KEYWORD, Keyword.VAR, UNKNOWN);
	private static final Token LOCAL = new Token(KEYWORD, Keyword.LOCAL, UNKNOWN);
	private static final Token GLOBAL = new Token(KEYWORD, Keyword.GLOBAL, UNKNOWN);
	private static final Token THIS = new Token(KEYWORD, Keyword.THIS, UNKNOWN);
	private static final Token FUNCTION = new Token(KEYWORD, Keyword.FUNCTION, UNKNOWN);
	private static final Token CLASS = new Token(KEYWORD, Keyword.CLASS, UNKNOWN);

	public static final Token BOOLEAN = new Token(TYPE, getCoercionType(Boolean.class), UNKNOWN);
	public static final Token BYTE = new Token(TYPE, getCoercionType(Byte.class), UNKNOWN);
	public static final Token CHAR = new Token(TYPE, getCoercionType(Character.class), UNKNOWN);
	public static final Token SHORT = new Token(TYPE, getCoercionType(Short.class), UNKNOWN);
	public static final Token INT = new Token(TYPE, getCoercionType(Integer.class), UNKNOWN);
	public static final Token LONG = new Token(TYPE, getCoercionType(Long.class), UNKNOWN);
	public static final Token BIG_INT = new Token(TYPE, getCoercionType(BigInteger.class), UNKNOWN);
	public static final Token FLOAT = new Token(TYPE, getCoercionType(Float.class), UNKNOWN);
	public static final Token DOUBLE = new Token(TYPE, getCoercionType(Double.class), UNKNOWN);
	public static final Token DECIMAL = new Token(TYPE, getCoercionType(BigDecimal.class), UNKNOWN);
	public static final Token STRING = new Token(TYPE, getCoercionType(String.class), UNKNOWN);

	private static final Token ASSIGN = new Token(OPERATOR, OperatorType.ASSIGN, UNKNOWN);
	private static final Token ADD = new Token(OPERATOR, OperatorType.ADD, UNKNOWN);
	private static final Token ADD_EQ = new Token(OPERATOR, OperatorType.ADD_EQ, UNKNOWN);
	private static final Token INCREMENT = new Token(OPERATOR, OperatorType.INCREMENT, UNKNOWN);
	private static final Token SUBSTRACT = new Token(OPERATOR, OperatorType.SUBSTRACT, UNKNOWN);
	private static final Token SUBSTRACT_EQ = new Token(OPERATOR, OperatorType.SUBSTRACT_EQ, UNKNOWN);
	private static final Token DECREMENT = new Token(OPERATOR, OperatorType.DECREMENT, UNKNOWN);
	private static final Token MULTIPLY = new Token(OPERATOR, OperatorType.MULTIPLY, UNKNOWN);
	private static final Token MULTIPLY_EQ = new Token(OPERATOR, OperatorType.MULTIPLY_EQ, UNKNOWN);
	private static final Token DIVIDE = new Token(OPERATOR, OperatorType.DIVIDE, UNKNOWN);
	private static final Token DIVIDE_EQ = new Token(OPERATOR, OperatorType.DIVIDE_EQ, UNKNOWN);
	private static final Token MODULO = new Token(OPERATOR, OperatorType.MODULO, UNKNOWN);
	private static final Token MODULO_EQ = new Token(OPERATOR, OperatorType.MODULO_EQ, UNKNOWN);
	private static final Token NOT = new Token(OPERATOR, OperatorType.NOT, UNKNOWN);
	private static final Token AND = new Token(OPERATOR, OperatorType.AND, UNKNOWN);
	private static final Token OR = new Token(OPERATOR, OperatorType.OR, UNKNOWN);
	private static final Token BIT_NOT = new Token(OPERATOR, OperatorType.BIT_NOT, UNKNOWN);
	private static final Token BIT_AND = new Token(OPERATOR, OperatorType.BIT_AND, UNKNOWN);
	private static final Token BIT_AND_EQ = new Token(OPERATOR, OperatorType.BIT_AND_EQ, UNKNOWN);
	private static final Token BIT_OR = new Token(OPERATOR, OperatorType.BIT_OR, UNKNOWN);
	private static final Token BIT_OR_EQ = new Token(OPERATOR, OperatorType.BIT_OR_EQ, UNKNOWN);
	private static final Token BIT_XOR = new Token(OPERATOR, OperatorType.BIT_XOR, UNKNOWN);
	private static final Token BIT_XOR_EQ = new Token(OPERATOR, OperatorType.BIT_XOR_EQ, UNKNOWN);
	private static final Token IS_GREATER = new Token(OPERATOR, OperatorType.IS_GREATER, UNKNOWN);
	private static final Token IS_GREATER_OR_EQ = new Token(OPERATOR, OperatorType.IS_GREATER_OR_EQ, UNKNOWN);
	private static final Token IS_LESS = new Token(OPERATOR, OperatorType.IS_LESS, UNKNOWN);
	private static final Token IS_LESS_OR_EQ = new Token(OPERATOR, OperatorType.IS_LESS_OR_EQ, UNKNOWN);
	private static final Token IS_EQUAL = new Token(OPERATOR, OperatorType.IS_EQUAL, UNKNOWN);
	private static final Token IS_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_NOT_EQUAL, UNKNOWN);
	private static final Token IS_REF_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_EQUAL, UNKNOWN);
	private static final Token IS_REF_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_NOT_EQUAL, UNKNOWN);
	private static final Token SHIFT_LEFT = new Token(OPERATOR, OperatorType.SHIFT_LEFT, UNKNOWN);
	private static final Token SHIFT_LEFT_EQ = new Token(OPERATOR, OperatorType.SHIFT_LEFT_EQ, UNKNOWN);
	private static final Token SHIFT_RIGHT = new Token(OPERATOR, OperatorType.SHIFT_RIGHT, UNKNOWN);
	private static final Token SHIFT_RIGHT_EQ = new Token(OPERATOR, OperatorType.SHIFT_RIGHT_EQ, UNKNOWN);

	private static final Token NULL = new Token(CONSTANT, null, UNKNOWN);
	private static final Token TRUE = new Token(CONSTANT, Boolean.TRUE, UNKNOWN);
	private static final Token FALSE = new Token(CONSTANT, Boolean.FALSE, UNKNOWN);

	public static final int OPERATOR_HASH_SIZE = 64;
	private static final Map<String, Token> OPERATOR_TOKEN_MAP = new HashMap<String, Token>(OPERATOR_HASH_SIZE);

	static {
		OPERATOR_TOKEN_MAP.put("=", ASSIGN);
		OPERATOR_TOKEN_MAP.put("+", ADD);
		OPERATOR_TOKEN_MAP.put("+=", ADD_EQ);
		OPERATOR_TOKEN_MAP.put("++", INCREMENT);
		OPERATOR_TOKEN_MAP.put("-", SUBSTRACT);
		OPERATOR_TOKEN_MAP.put("-=", SUBSTRACT_EQ);
		OPERATOR_TOKEN_MAP.put("--", DECREMENT);
		OPERATOR_TOKEN_MAP.put("*", MULTIPLY);
		OPERATOR_TOKEN_MAP.put("*=", MULTIPLY_EQ);
		OPERATOR_TOKEN_MAP.put("/", DIVIDE);
		OPERATOR_TOKEN_MAP.put("/=", DIVIDE_EQ);
		OPERATOR_TOKEN_MAP.put("%", MODULO);
		OPERATOR_TOKEN_MAP.put("%=", MODULO_EQ);
		OPERATOR_TOKEN_MAP.put("!", NOT);
		OPERATOR_TOKEN_MAP.put("&&", AND);
		OPERATOR_TOKEN_MAP.put("||", OR);
		OPERATOR_TOKEN_MAP.put("~", BIT_NOT);
		OPERATOR_TOKEN_MAP.put("&", BIT_AND);
		OPERATOR_TOKEN_MAP.put("&=", BIT_AND_EQ);
		OPERATOR_TOKEN_MAP.put("|", BIT_OR);
		OPERATOR_TOKEN_MAP.put("|=", BIT_OR_EQ);
		OPERATOR_TOKEN_MAP.put("^", BIT_XOR);
		OPERATOR_TOKEN_MAP.put("^=", BIT_XOR_EQ);
		OPERATOR_TOKEN_MAP.put(">", IS_GREATER);
		OPERATOR_TOKEN_MAP.put(">=", IS_GREATER_OR_EQ);
		OPERATOR_TOKEN_MAP.put("<", IS_LESS);
		OPERATOR_TOKEN_MAP.put("<=", IS_LESS_OR_EQ);
		OPERATOR_TOKEN_MAP.put("==", IS_EQUAL);
		OPERATOR_TOKEN_MAP.put("===", IS_REF_EQUAL);
		OPERATOR_TOKEN_MAP.put("!==", IS_REF_NOT_EQUAL);
		OPERATOR_TOKEN_MAP.put("!=", IS_NOT_EQUAL);
		OPERATOR_TOKEN_MAP.put("<<", SHIFT_LEFT);
		OPERATOR_TOKEN_MAP.put("<<=", SHIFT_LEFT_EQ);
		OPERATOR_TOKEN_MAP.put(">>", SHIFT_RIGHT);
		OPERATOR_TOKEN_MAP.put(">>=", SHIFT_RIGHT_EQ);
	}

	private static final int KEYWORD_HASH_SIZE = 64;
	private static final Map<String, Token> KEYWORD_TOKEN_MAP = new HashMap<String, Token>(KEYWORD_HASH_SIZE);

	static {
		KEYWORD_TOKEN_MAP.put("null", NULL);
		KEYWORD_TOKEN_MAP.put("true", TRUE);
		KEYWORD_TOKEN_MAP.put("false", FALSE);

		KEYWORD_TOKEN_MAP.put("if", IF);
		KEYWORD_TOKEN_MAP.put("else", ELSE);
		KEYWORD_TOKEN_MAP.put("for", FOR);
		KEYWORD_TOKEN_MAP.put("break", BREAK);
		KEYWORD_TOKEN_MAP.put("continue", CONTINUE);
		KEYWORD_TOKEN_MAP.put("fallthrough", FALLTHROUGH);
		KEYWORD_TOKEN_MAP.put("return", RETURN);
		KEYWORD_TOKEN_MAP.put("do", DO);
		KEYWORD_TOKEN_MAP.put("while", WHILE);
		KEYWORD_TOKEN_MAP.put("switch", SWITCH);
		KEYWORD_TOKEN_MAP.put("case", CASE);
		KEYWORD_TOKEN_MAP.put("try", TRY);
		KEYWORD_TOKEN_MAP.put("catch", CATCH);
		KEYWORD_TOKEN_MAP.put("finally", FINALLY);
		KEYWORD_TOKEN_MAP.put("var", VAR);
		KEYWORD_TOKEN_MAP.put("local", LOCAL);
		KEYWORD_TOKEN_MAP.put("global", GLOBAL);
		KEYWORD_TOKEN_MAP.put("this", THIS);
		KEYWORD_TOKEN_MAP.put("function", FUNCTION);
		KEYWORD_TOKEN_MAP.put("class", CLASS);

		KEYWORD_TOKEN_MAP.put("boolean", BOOLEAN);
		KEYWORD_TOKEN_MAP.put("byte", BYTE);
		KEYWORD_TOKEN_MAP.put("short", SHORT);
		KEYWORD_TOKEN_MAP.put("char", CHAR);
		KEYWORD_TOKEN_MAP.put("int", INT);
		KEYWORD_TOKEN_MAP.put("long", LONG);
		KEYWORD_TOKEN_MAP.put("bigint", BIG_INT);
		KEYWORD_TOKEN_MAP.put("float", FLOAT);
		KEYWORD_TOKEN_MAP.put("double", DOUBLE);
		KEYWORD_TOKEN_MAP.put("decimal", DECIMAL);
		KEYWORD_TOKEN_MAP.put("string", STRING);
	}

	private static final int GRAMMAR_HASH_SIZE = 16;
	private static final Map<String, Token> GRAMMAR_TOKEN_MAP = new HashMap<String, Token>(GRAMMAR_HASH_SIZE);

	static {
		GRAMMAR_TOKEN_MAP.put(".", SCOPE);
		GRAMMAR_TOKEN_MAP.put(",", COMMA);
		GRAMMAR_TOKEN_MAP.put(":", COLON);
		GRAMMAR_TOKEN_MAP.put("?", QUEST);
		GRAMMAR_TOKEN_MAP.put("..", RANGE);
		GRAMMAR_TOKEN_MAP.put("->", ARROW);
		GRAMMAR_TOKEN_MAP.put("=>", LAMBDA);
		GRAMMAR_TOKEN_MAP.put(";", SEMICOL);
		GRAMMAR_TOKEN_MAP.put("(", O_BRACK);
		GRAMMAR_TOKEN_MAP.put(")", C_BRACK);
		GRAMMAR_TOKEN_MAP.put("[", O_INDEX);
		GRAMMAR_TOKEN_MAP.put("]", C_INDEX);
		GRAMMAR_TOKEN_MAP.put("{", O_BLOCK);
		GRAMMAR_TOKEN_MAP.put("}", C_BLOCK);
	}

	public static Token getOperationToken(final String aString, final SourcePosition aPosition) {
		return asToken(OPERATOR_TOKEN_MAP.get(aString), aPosition);
	}

	public static Token getKeywordToken(final String aString, final SourcePosition aPosition) {
		final Token keyword = KEYWORD_TOKEN_MAP.get(aString);

		if (keyword == null) {
			return null;
		}

		return asToken(keyword, aPosition);
	}

	public static Token getSyntaxToken(final String aString, final SourcePosition aPosition) {
		return asToken(GRAMMAR_TOKEN_MAP.get(aString), aPosition);
	}

	private static Token asToken(final Token aToken, final SourcePosition aPosition) {
		return new Token(aToken.getType(), aToken.getValue(), aPosition);
	}
}
