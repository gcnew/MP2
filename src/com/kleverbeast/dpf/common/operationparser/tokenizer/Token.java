package com.kleverbeast.dpf.common.operationparser.tokenizer;

public class Token {
	private final TokenTypes mType;
	private final Object mValue;

	public Token(final TokenTypes aType, final Object aValue) {
		mType = aType;
		mValue = aValue;
	}

	public TokenTypes getType() {
		return mType;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) mValue;
	}

	public String getStringValue() {
		return (String) mValue;
	}

	public String toString() {
		return "Token [mType=" + mType + ", mValue=" + mValue + "]";
	}
}
