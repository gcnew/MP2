package com.kleverbeast.dpf.common.operationparser.tokenizer;

import static com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.getCoercionType;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes.CONSTANT;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes.GRAMMAR_TOKEN;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes.KEYWORD;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes.OPERATOR;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes.TYPE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TokenConstants {
	public static final Token SCOPE = new Token(GRAMMAR_TOKEN, GrammarTokens.SCOPE);
	public static final Token COMMA = new Token(GRAMMAR_TOKEN, GrammarTokens.COMMA);
	public static final Token SEMICOL = new Token(GRAMMAR_TOKEN, GrammarTokens.SEMICOL);
	public static final Token O_BRACK = new Token(GRAMMAR_TOKEN, GrammarTokens.O_BRACK);
	public static final Token C_BRACK = new Token(GRAMMAR_TOKEN, GrammarTokens.C_BRACK);
	public static final Token O_INDEX = new Token(GRAMMAR_TOKEN, GrammarTokens.O_INDEX);
	public static final Token C_INDEX = new Token(GRAMMAR_TOKEN, GrammarTokens.C_INDEX);
	public static final Token O_BLOCK = new Token(GRAMMAR_TOKEN, GrammarTokens.O_BLOCK);
	public static final Token C_BLOCK = new Token(GRAMMAR_TOKEN, GrammarTokens.C_BLOCK);

	public static final Token IF = new Token(KEYWORD, Keywords.IF);
	public static final Token ELSE = new Token(KEYWORD, Keywords.ELSE);
	public static final Token FOR = new Token(KEYWORD, Keywords.FOR);
	public static final Token BREAK = new Token(KEYWORD, Keywords.BREAK);
	public static final Token CONTINUE = new Token(KEYWORD, Keywords.CONTINUE);
	public static final Token DO = new Token(KEYWORD, Keywords.DO);
	public static final Token WHILE = new Token(KEYWORD, Keywords.WHILE);
	public static final Token TRY = new Token(KEYWORD, Keywords.TRY);
	public static final Token CATCH = new Token(KEYWORD, Keywords.CATCH);
	public static final Token FINALLY = new Token(KEYWORD, Keywords.FINALLY);
	public static final Token FUNCTION = new Token(KEYWORD, Keywords.FUNCTION);
	public static final Token CLASS = new Token(KEYWORD, Keywords.CLASS);

	public static final Token BOOLEAN = new Token(TYPE, getCoercionType(Boolean.class));
	public static final Token BYTE = new Token(TYPE, getCoercionType(Byte.class));
	public static final Token CHAR = new Token(TYPE, getCoercionType(Character.class));
	public static final Token SHORT = new Token(TYPE, getCoercionType(Short.class));
	public static final Token INT = new Token(TYPE, getCoercionType(Integer.class));
	public static final Token LONG = new Token(TYPE, getCoercionType(Long.class));
	public static final Token BIG_INT = new Token(TYPE, getCoercionType(BigInteger.class));
	public static final Token FLOAT = new Token(TYPE, getCoercionType(Float.class));
	public static final Token DOUBLE = new Token(TYPE, getCoercionType(Double.class));
	public static final Token DECIMAL = new Token(TYPE, getCoercionType(BigDecimal.class));
	public static final Token STRING = new Token(TYPE, getCoercionType(String.class));

	private static final Token ASSIGN = new Token(OPERATOR, OperatorType.ASSIGN);
	private static final Token ADD = new Token(OPERATOR, OperatorType.ADD);
	private static final Token ADD_EQ = new Token(OPERATOR, OperatorType.ADD_EQ);
	private static final Token SUBSTRACT = new Token(OPERATOR, OperatorType.SUBSTRACT);
	private static final Token SUBSTRACT_EQ = new Token(OPERATOR, OperatorType.SUBSTRACT_EQ);
	private static final Token MULTIPLY = new Token(OPERATOR, OperatorType.MULTIPLY);
	private static final Token MULTIPLY_EQ = new Token(OPERATOR, OperatorType.MULTIPLY_EQ);
	private static final Token DIVIDE = new Token(OPERATOR, OperatorType.DIVIDE);
	private static final Token DIVIDE_EQ = new Token(OPERATOR, OperatorType.DIVIDE_EQ);
	private static final Token MODULO = new Token(OPERATOR, OperatorType.MODULO);
	private static final Token MODULO_EQ = new Token(OPERATOR, OperatorType.MODULO_EQ);
	private static final Token NOT = new Token(OPERATOR, OperatorType.NOT);
	private static final Token AND = new Token(OPERATOR, OperatorType.AND);
	private static final Token OR = new Token(OPERATOR, OperatorType.OR);
	private static final Token BIT_NOT = new Token(OPERATOR, OperatorType.BIT_NOT);
	private static final Token BIT_AND = new Token(OPERATOR, OperatorType.BIT_AND);
	private static final Token BIT_AND_EQ = new Token(OPERATOR, OperatorType.BIT_AND_EQ);
	private static final Token BIT_OR = new Token(OPERATOR, OperatorType.BIT_OR);
	private static final Token BIT_OR_EQ = new Token(OPERATOR, OperatorType.BIT_OR_EQ);
	private static final Token BIT_XOR = new Token(OPERATOR, OperatorType.BIT_XOR);
	private static final Token BIT_XOR_EQ = new Token(OPERATOR, OperatorType.BIT_XOR_EQ);
	private static final Token IS_GREATER = new Token(OPERATOR, OperatorType.IS_GREATER);
	private static final Token IS_GREATER_OR_EQ = new Token(OPERATOR, OperatorType.IS_GREATER_OR_EQ);
	private static final Token IS_LESS = new Token(OPERATOR, OperatorType.IS_LESS);
	private static final Token IS_LESS_OR_EQ = new Token(OPERATOR, OperatorType.IS_LESS_OR_EQ);
	private static final Token IS_EQUAL = new Token(OPERATOR, OperatorType.IS_EQUAL);
	private static final Token IS_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_NOT_EQUAL);
	private static final Token IS_REF_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_EQUAL);
	private static final Token IS_REF_NOT_EQUAL = new Token(OPERATOR, OperatorType.IS_REF_NOT_EQUAL);
	private static final Token SHIFT_LEFT = new Token(OPERATOR, OperatorType.SHIFT_LEFT);
	private static final Token SHIFT_LEFT_EQ = new Token(OPERATOR, OperatorType.SHIFT_LEFT_EQ);
	private static final Token SHIFT_RIGHT = new Token(OPERATOR, OperatorType.SHIFT_RIGHT);
	private static final Token SHIFT_RIGHT_EQ = new Token(OPERATOR, OperatorType.SHIFT_RIGHT_EQ);

	private static final Token NULL = new Token(CONSTANT, null);
	private static final Token TRUE = new Token(CONSTANT, Boolean.TRUE);
	private static final Token FALSE = new Token(CONSTANT, Boolean.FALSE);

	public static final int OPERATOR_HASH_SIZE = 64;
	private static final Map<String, Token> OPERATOR_TOKEN_MAP = new HashMap<String, Token>(OPERATOR_HASH_SIZE);

	static {
		OPERATOR_TOKEN_MAP.put("=", ASSIGN);
		OPERATOR_TOKEN_MAP.put("+", ADD);
		OPERATOR_TOKEN_MAP.put("+=", ADD_EQ);
		OPERATOR_TOKEN_MAP.put("-", SUBSTRACT);
		OPERATOR_TOKEN_MAP.put("-=", SUBSTRACT_EQ);
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
		OPERATOR_TOKEN_MAP.put("=", ASSIGN);
		OPERATOR_TOKEN_MAP.put("!=", IS_NOT_EQUAL);
		OPERATOR_TOKEN_MAP.put("<<", SHIFT_LEFT);
		OPERATOR_TOKEN_MAP.put("<<=", SHIFT_LEFT_EQ);
		OPERATOR_TOKEN_MAP.put(">>", SHIFT_RIGHT);
		OPERATOR_TOKEN_MAP.put(">>=", SHIFT_RIGHT_EQ);
	}

	private static final int KEYWORD_HASH_SIZE = 32;
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
		KEYWORD_TOKEN_MAP.put("do", DO);
		KEYWORD_TOKEN_MAP.put("while", WHILE);
		KEYWORD_TOKEN_MAP.put("try", TRY);
		KEYWORD_TOKEN_MAP.put("catch", CATCH);
		KEYWORD_TOKEN_MAP.put("finally", FINALLY);
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
	private static final Map<Character, Token> GRAMMAR_TOKEN_MAP = new HashMap<Character, Token>(GRAMMAR_HASH_SIZE);

	static {
		GRAMMAR_TOKEN_MAP.put('.', SCOPE);
		GRAMMAR_TOKEN_MAP.put(',', COMMA);
		GRAMMAR_TOKEN_MAP.put(';', SEMICOL);
		GRAMMAR_TOKEN_MAP.put('(', O_BRACK);
		GRAMMAR_TOKEN_MAP.put(')', C_BRACK);
		GRAMMAR_TOKEN_MAP.put('[', O_INDEX);
		GRAMMAR_TOKEN_MAP.put(']', C_INDEX);
		GRAMMAR_TOKEN_MAP.put('{', O_BLOCK);
		GRAMMAR_TOKEN_MAP.put('}', C_BLOCK);
	}

	public static Token getOperationToken(final String aString) {
		return OPERATOR_TOKEN_MAP.get(aString);
	}

	public static Token getKeywordToken(final String aString) {
		return KEYWORD_TOKEN_MAP.get(aString);
	}

	public static Token getGrammarToken(final Character aChar) {
		return GRAMMAR_TOKEN_MAP.get(aChar);
	}
}
