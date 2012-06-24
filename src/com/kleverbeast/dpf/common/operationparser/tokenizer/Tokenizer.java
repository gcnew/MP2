package com.kleverbeast.dpf.common.operationparser.tokenizer;

import java.util.ArrayList;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.exception.ParsingException;

public class Tokenizer {
	private int mIndex;
	private final String mSource;
	private final List<Token> mTokens;

	public Tokenizer(final String aSource) {
		mSource = aSource;
		mTokens = new ArrayList<Token>();
	}

	public int getPostion() {
		return mIndex;
	}

	public void restorePosition(final int aPosition) {
		if ((aPosition >= 0) && (aPosition < mTokens.size())) {
			mIndex = aPosition;

			return;
		}

		throw new IllegalArgumentException("Inappropriate position: " + aPosition);
	}

	private void skipWhiteSpaces() {
		while ((mIndex < mSource.length()) && Character.isWhitespace(mSource.charAt(mIndex))) {
			++mIndex;
		}
	}

	public void tokenize() throws ParsingException {
		while (hasNext0()) {
			mTokens.add(next0());
		}

		mIndex = 0;
	}

	private boolean hasNext0() {
		skipWhiteSpaces();

		return mIndex != mSource.length();
	}

	private Token next0() throws ParsingException {
		final char c = mSource.charAt(mIndex);

		switch (c) {
		case '.':
		case ',':
		case ';':
		case '(':
		case ')':
		case '[':
		case ']':
		case '{':
		case '}':
			++mIndex;
			return TokenConstants.getGrammarToken(c);
		case '\'':
			return parseString();
		case '+':
		case '-':
		case '/':
		case '*':
		case '%':
		case '^':
		case '~':
			return parseOneOrEqual();
		case '!':
		case '=':
			return parseRefOrEqual();
		case '&':
		case '|':
		case '>':
		case '<':
			return parseTwoOrEqual();
		case '$':
			return parseVariable();
		}

		if (Character.isDigit(c)) {
			return parseNumber();
		}

		return parseLiteral();
	}

	public boolean hasNext() throws ParsingException {
		return mIndex < mTokens.size();
	}

	public Token next() throws ParsingException {
		if (!hasNext()) {
			throw new ParsingException("EOF reached");
		}

		return mTokens.get(mIndex++);
	}

	private Token parseString() throws ParsingException {
		final int endIndex = mSource.indexOf('\'', mIndex + 1);

		if (endIndex < 0) {
			throw new ParsingException("No closing quote found for string starting at index: " + mIndex);
		}

		final Token retval = new Token(TokenTypes.CONSTANT, mSource.substring(++mIndex, endIndex));
		mIndex = endIndex + 1;

		return retval;
	}

	private Token parseNumber() {
		int endIndex = mIndex;

		do {
			++endIndex;
		} while ((endIndex < mSource.length()) && Character.isDigit(mSource.charAt(endIndex)));

		final String number = mSource.substring(mIndex, endIndex);
		final Token retval = new Token(TokenTypes.CONSTANT, Integer.valueOf(number));
		mIndex = endIndex;

		return retval;
	}

	private int findLiteralEnd() {
		int endIndex = mIndex;

		do {
			++endIndex;
		} while ((endIndex < mSource.length()) && Character.isLetterOrDigit(mSource.charAt(endIndex)));

		return endIndex;
	}

	private Token parseVariable() throws ParsingException {
		++mIndex;
		if (mIndex == mSource.length()) {
			throw new ParsingException("Invalid variable sigil found at end of file");
		}

		if (!Character.isLetter(mSource.charAt(mIndex))) {
			throw new ParsingException("Invalid character '" + mSource.charAt(mIndex) + "' found after variable sigil");
		}

		final int endIndex = findLiteralEnd();
		final Token retval = new Token(TokenTypes.LITERAL, mSource.substring(--mIndex, endIndex));
		mIndex = endIndex;

		return retval;
	}

	private Token parseLiteral() {
		final int endIndex = findLiteralEnd();
		final String literal = mSource.substring(mIndex, endIndex);

		final Token keyword = TokenConstants.getKeywordToken(literal);
		final Token retval = (keyword != null) ? keyword : new Token(TokenTypes.LITERAL, literal);
		mIndex = endIndex;

		return retval;
	}

	private boolean isCharAtOffset(final int aOffset, final char aChar) {
		return ((mIndex + aOffset) < mSource.length()) && (mSource.charAt(mIndex + aOffset) == aChar);
	}

	private Token parseOneOrEqual() {
		final int advance = isCharAtOffset(1, '=') ? 2 : 1;
		mIndex += advance;

		return TokenConstants.getOperationToken(mSource.substring(mIndex - advance, mIndex));
	}

	private Token parseTwoOrEqual() {
		if (isCharAtOffset(1, mSource.charAt(mIndex))) {
			final int advance = isCharAtOffset(2, '=') ? 3 : 2;
			mIndex += advance;

			return TokenConstants.getOperationToken(mSource.substring(mIndex - advance, mIndex));
		}

		return parseOneOrEqual();
	}

	private Token parseRefOrEqual() {
		if (isCharAtOffset(2, '=') && isCharAtOffset(1, '=')) {
			mIndex += 3;

			return TokenConstants.getOperationToken(mSource.substring(mIndex - 3, mIndex));
		}

		return parseOneOrEqual();
	}
}
