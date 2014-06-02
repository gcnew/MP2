package re.agiledesign.mp2.lexer;

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
	private static final Token SCOPE = new Token(SYNTAX_TOKEN, SyntaxToken.SCOPE, 0, 0);
	private static final Token COMMA = new Token(SYNTAX_TOKEN, SyntaxToken.COMMA, 0, 0);
	private static final Token COLON = new Token(SYNTAX_TOKEN, SyntaxToken.COLON, 0, 0);
	private static final Token QUEST = new Token(SYNTAX_TOKEN, SyntaxToken.QUEST, 0, 0);
	private static final Token RANGE = new Token(SYNTAX_TOKEN, SyntaxToken.RANGE, 0, 0);
	private static final Token ARROW = new Token(SYNTAX_TOKEN, SyntaxToken.ARROW, 0, 0);
	private static final Token LAMBDA = new Token(SYNTAX_TOKEN, SyntaxToken.LAMBDA, 0, 0);
	private static final Token SEMICOL = new Token(SYNTAX_TOKEN, SyntaxToken.SEMICOL, 0, 0);
	private static final Token O_BRACK = new Token(SYNTAX_TOKEN, SyntaxToken.O_BRACK, 0, 0);
	private static final Token C_BRACK = new Token(SYNTAX_TOKEN, SyntaxToken.C_BRACK, 0, 0);
	private static final Token O_INDEX = new Token(SYNTAX_TOKEN, SyntaxToken.O_INDEX, 0, 0);
	private static final Token C_INDEX = new Token(SYNTAX_TOKEN, SyntaxToken.C_INDEX, 0, 0);
	private static final Token O_BLOCK = new Token(SYNTAX_TOKEN, SyntaxToken.O_BLOCK, 0, 0);
	private static final Token C_BLOCK = new Token(SYNTAX_TOKEN, SyntaxToken.C_BLOCK, 0, 0);

	private static final Token IF = new Token(KEYWORD, Keyword.IF, 0, 0);
	private static final Token ELSE = new Token(KEYWORD, Keyword.ELSE, 0, 0);
	private static final Token FOR = new Token(KEYWORD, Keyword.FOR, 0, 0);
	private static final Token BREAK = new Token(KEYWORD, Keyword.BREAK, 0, 0);
	private static final Token CONTINUE = new Token(KEYWORD, Keyword.CONTINUE, 0, 0);
	private static final Token RETURN = new Token(KEYWORD, Keyword.RETURN, 0, 0);
	private static final Token DO = new Token(KEYWORD, Keyword.DO, 0, 0);
	private static final Token WHILE = new Token(KEYWORD, Keyword.WHILE, 0, 0);
	private static final Token SWITCH = new Token(KEYWORD, Keyword.SWITCH, 0, 0);
	private static final Token CASE = new Token(KEYWORD, Keyword.CASE, 0, 0);
	private static final Token TRY = new Token(KEYWORD, Keyword.TRY, 0, 0);
	private static final Token CATCH = new Token(KEYWORD, Keyword.CATCH, 0, 0);
	private static final Token FINALLY = new Token(KEYWORD, Keyword.FINALLY, 0, 0);
	private static final Token VAR = new Token(KEYWORD, Keyword.VAR, 0, 0);
	private static final Token LOCAL = new Token(KEYWORD, Keyword.LOCAL, 0, 0);
	private static final Token GLOBAL = new Token(KEYWORD, Keyword.GLOBAL, 0, 0);
	private static final Token THIS = new Token(KEYWORD, Keyword.THIS, 0, 0);
	private static final Token FUNCTION = new Token(KEYWORD, Keyword.FUNCTION, 0, 0);
	private static final Token CLASS = new Token(KEYWORD, Keyword.CLASS, 0, 0);

	public static final Token BOOLEAN = new Token(TYPE, getCoercionType(Boolean.class), 0, 0);
	public static final Token BYTE = new Token(TYPE, getCoercionType(Byte.class), 0, 0);
	public static final Token CHAR = new Token(TYPE, getCoercionType(Character.class), 0, 0);
	public static final Token SHORT = new Token(TYPE, getCoercionType(Short.class), 0, 0);
	public static final Token INT = new Token(TYPE, getCoercionType(Integer.class), 0, 0);
	public static final Token LONG = new Token(TYPE, getCoercionType(Long.class), 0, 0);
	public static final Token BIG_INT = new Token(TYPE, getCoercionType(BigInteger.class), 0, 0);
	public static final Token FLOAT = new Token(TYPE, getCoercionType(Float.class), 0, 0);
	public static final Token DOUBLE = new Token(TYPE, getCoercionType(Double.class), 0, 0);
	public static final Token DECIMAL = new Token(TYPE, getCoercionType(BigDecimal.class), 0, 0);
	public static final Token STRING = new Token(TYPE, getCoercionType(String.class), 0, 0);

	private static final Token ASSIGN = new Token(OPERATOR, OperatorType.ASSIGN, 0, 0);
	private static final Token ADD = new Token(OPERATOR, OperatorType.ADD, 0, 0);
	private static final Token ADD_EQ = new Token(OPERATOR, OperatorType.ADD_EQ, 0, 0);
	private static final Token INCREMENT = new Token(OPERATOR, OperatorType.INCREMENT, 0, 0);
	private static final Token SUBSTRACT = new Token(OPERATOR, OperatorType.SUBSTRACT, 0, 0);
	private static final Token SUBSTRACT_EQ = new Token(OPERATOR, OperatorType.SUBSTRACT_EQ, 0, 0);
	private static final Token DECREMENT = new Token(OPERATOR, OperatorType.DECREMENT, 0, 0);
	private static final Token MULTIPLY = new Token(OPERATOR, OperatorType.MULTIPLY, 0, 0);
	private static final Token MULTIPLY_EQ = new Token(OPERATOR, OperatorType.MULTIPLY_EQ, 0, 0);
	private static final Token DIVIDE = new Token(OPERATOR, OperatorType.DIVIDE, 0, 0);
	private static final Token DIVIDE_EQ = new Token(OPERATOR, OperatorType.DIVIDE_EQ, 0, 0);
	private static final Token MODULO = new Token(OPERATOR, OperatorType.MODULO, 0, 0);
	private static final Token MODULO_EQ = new Token(OPERATOR, OperatorType.MODULO_EQ, 0, 0);
	private static final Token NOT = new Token(OPERATOR, OperatorType.NOT, 0, 0);
	private static final Token AND = new Token(OPERATOR, OperatorType.AND, 0, 0);
	private static final Token OR = new Token(OPERATOR, OperatorType.OR, 0, 0);
	private static final Token BIT_NOT = new Token(OPERATOR, OperatorType.BIT_NOT, 0, 0);
	private static final Token BIT_AND = new Token(OPERATOR, OperatorType.BIT_AND, 0, 0);
	private static final Token BIT_AND_EQ = new Token(OPERATOR, OperatorType.BIT_AND_EQ, 0, 0);
	private static final Token BIT_OR = new Token(OPERATOR, OperatorType.BIT_OR, 0, 0);
	private static final Token BIT_OR_EQ = new Token(OPERATOR, OperatorType.BIT_OR_EQ, 0, 0);
	private static final Token BIT_XOR = new Token(OPERATOR, OperatorType.BIT_XOR, 0, 0);
	private static final Token BIT_XOR_EQ = new Token(OPERATOR, OperatorType.BIT_XOR_EQ, 0, 0);
	private static final Token IS_GREATER = new Token(OPERATOR, OperatorType.IS_GREATER, 0, 0);
	private static final Token IS_GREATER_OR_EQ = new Token(OPERATOR, OperatorType.IS_GREATER_OR_EQ, 0, 0);
	private static final Token IS_LESS = new Token(OPERATOR, OperatorType.IS_LESS, 0, 0);
	private static final Token IS_LESS_OR_EQ = new Token(OPERATOR, OperatorType.IS_LESS_OR_EQ, 0, 0);
	private static final Token IS_EQUAL = new Token(OPERATOR, OperatorType.IS_EQUAL, 0, 0);
	private static final Token IS_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_NOT_EQUAL, 0, 0);
	private static final Token IS_REF_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_EQUAL, 0, 0);
	private static final Token IS_REF_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_NOT_EQUAL, 0, 0);
	private static final Token SHIFT_LEFT = new Token(OPERATOR, OperatorType.SHIFT_LEFT, 0, 0);
	private static final Token SHIFT_LEFT_EQ = new Token(OPERATOR, OperatorType.SHIFT_LEFT_EQ, 0, 0);
	private static final Token SHIFT_RIGHT = new Token(OPERATOR, OperatorType.SHIFT_RIGHT, 0, 0);
	private static final Token SHIFT_RIGHT_EQ = new Token(OPERATOR, OperatorType.SHIFT_RIGHT_EQ, 0, 0);

	private static final Token NULL = new Token(CONSTANT, null, 0, 0);
	private static final Token TRUE = new Token(CONSTANT, Boolean.TRUE, 0, 0);
	private static final Token FALSE = new Token(CONSTANT, Boolean.FALSE, 0, 0);

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

	public static Token getOperationToken(final String aString, final int aLine, final int aCharNo) {
		return asToken(OPERATOR_TOKEN_MAP.get(aString), aLine, aCharNo);
	}

	public static Token getKeywordToken(final String aString, final int aLine, final int aCharNo) {
		final Token keyword = KEYWORD_TOKEN_MAP.get(aString);

		if (keyword == null) {
			return null;
		}

		return asToken(keyword, aLine, aCharNo);
	}

	public static Token getSyntaxToken(final String aString, final int aLine, final int aCharNo) {
		return asToken(GRAMMAR_TOKEN_MAP.get(aString), aLine, aCharNo);
	}

	private static Token asToken(final Token aToken, final int aLine, final int aCharNo) {
		return new Token(aToken.getType(), aToken.getValue(), aLine, aCharNo);
	}
}
