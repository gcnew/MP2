package re.agiledesign.mp2.tokenizer;

import java.util.ArrayList;
import java.util.List;

import re.agiledesign.mp2.exception.ParsingException;


public class Tokenizer {
	private int mIndex;
	private final boolean mInt32;
	private final String mSource;
	private final List<Token> mTokens;

	public Tokenizer(final String aSource, final boolean aInt32) {
		mInt32 = aInt32;
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
		while (!isAtEnd() && Character.isWhitespace(mSource.charAt(mIndex))) {
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

		return !isAtEnd();
	}

	private Token next0() throws ParsingException {
		final char c = mSource.charAt(mIndex);

		switch (c) {
		case '.':
			if (isCharAtOffset(1, '.')) {
				mIndex += 2;
				return TokenConstants.RANGE;
			}
			//$FALL-THROUGH$
		case ',':
		case ':':
		case '?':
		case ';':
		case '(':
		case ')':
		case '[':
		case ']':
		case '{':
		case '}':
			++mIndex;
			return TokenConstants.getGrammarToken(Character.valueOf(c));
		case '\'':
			return parseString();
		case '-':
			if (isCharAtOffset(1, '>')) {
				mIndex += 2;
				return TokenConstants.ARROW;
			}
			//$FALL-THROUGH$
		case '+':
		case '/':
		case '*':
		case '%':
		case '^':
		case '~':
			return parseOneOrEqual();
		case '!':
		case '=':
			return parseRefEqualOrLambda();
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

		return parseIdentifier();
	}

	public boolean hasNext() {
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

	private Token parseNumber() throws ParsingException {
		final int startIndex = mIndex;

		int base = 0; // 0 will be normalized to 10, used to help parsing floats
		if (isCharAtOffset(0, '0') && !isAtEnd(1)) {
			final char next = Character.toLowerCase(mSource.charAt(mIndex + 1));

			switch (next) {
			case 'x':
				base = 16;
				break;
			case 'd':
				base = 10;
				break;
			case 'o':
				base = 8;
				break;
			case 'b':
				base = 2;
				break;
			default:
				if (Character.isDigit(next)) {
					// don't allow mistakes to pass through
					throw new ParsingException("Numeral system code expected (x/o/b/d)");
				}

				mIndex -= 2;
			}

			mIndex += 2;
		}

		do {
			++mIndex;
		} while (!isAtEnd() && Character.isDigit(mSource.charAt(mIndex)));

		boolean isFloat = false;
		if (isCharAtOffset(0, '.')) {
			if (isAtEnd(1) || Character.isDigit(mSource.charAt(mIndex + 1))) {
				if (base != 0) {
					throw new ParsingException("Invalid floating point syntax (cannot have numeral system specifiers)");
				}

				isFloat = true;
				do {
					++mIndex;
				} while (!isAtEnd() && Character.isDigit(mSource.charAt(mIndex)));
			}
		}

		final Object constant;
		final String number = mSource.substring(startIndex, mIndex);
		if (isFloat) {
			constant = Double.valueOf(number);
		} else {
			base = (base == 0) ? 10 : base;

			if (mInt32) {
				constant = Integer.valueOf(Integer.parseInt(number, base));
			} else {
				constant = Long.valueOf(Long.parseLong(number, base));
			}
		}

		return new Token(TokenTypes.CONSTANT, constant);
	}

	private boolean isIdentifierChar(final char aChar) {
		return Character.isLetterOrDigit(aChar) || (aChar == '_');
	}

	private Token parseVariable() throws ParsingException {
		final int startIndex = mIndex++;

		if (isAtEnd()) {
			throw new ParsingException("Identifier expected but end of file reached");
		}

		parseIdentifier();
		return new Token(TokenTypes.IDENTIFIER, mSource.substring(startIndex, mIndex));
	}

	private Token parseIdentifier() throws ParsingException {
		final int startIndex = mIndex;

		final char firstChar = mSource.charAt(mIndex);
		if (!(Character.isLetter(firstChar) || (firstChar == '_'))) {
			throw new ParsingException("Identifier starting character expected but found: " + firstChar);
		}

		do {
			++mIndex;
		} while (!isAtEnd() && isIdentifierChar(mSource.charAt(mIndex)));

		final String identifier = mSource.substring(startIndex, mIndex);
		final Token keyword = TokenConstants.getKeywordToken(identifier);
		return (keyword != null) ? keyword : new Token(TokenTypes.IDENTIFIER, identifier);
	}

	private boolean isCharAtOffset(final int aOffset, final char aChar) {
		return !isAtEnd(aOffset) && (mSource.charAt(mIndex + aOffset) == aChar);
	}

	private boolean isAtEnd() {
		return isAtEnd(0);
	}

	private boolean isAtEnd(final int aOffset) {
		return (mIndex + aOffset) >= mSource.length();
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

	private Token parseRefEqualOrLambda() {
		if (isCharAtOffset(0, '=') && isCharAtOffset(1, '>')) {
			mIndex += 2;

			return TokenConstants.LAMBDA;
		}

		if (isCharAtOffset(2, '=') && isCharAtOffset(1, '=')) {
			mIndex += 3;

			return TokenConstants.getOperationToken(mSource.substring(mIndex - 3, mIndex));
		}

		return parseOneOrEqual();
	}
}
