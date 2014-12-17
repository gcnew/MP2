package re.agiledesign.mp2.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.lexer.RegExpInfo.Flag;
import re.agiledesign.mp2.util.ArrayUtil;
import re.agiledesign.mp2.util.AssertUtil;
import re.agiledesign.mp2.util.StringUtil;

public class Tokenizer {
	private int mChar;
	private int mLine;
	private int mIndex;
	private final String mSource;

	private int mTokenIndex;
	private int mTokenLine;
	private int mTokenChar;

	private static final Map<Character, Integer> RADIX_MAP = new HashMap<Character, Integer>(8);

	static {
		RADIX_MAP.put(Character.valueOf('x'), Integer.valueOf(16));
		RADIX_MAP.put(Character.valueOf('d'), Integer.valueOf(10));
		RADIX_MAP.put(Character.valueOf('o'), Integer.valueOf(8));
		RADIX_MAP.put(Character.valueOf('b'), Integer.valueOf(2));
	}

	public Tokenizer(final String aSource) {
		mSource = aSource;
	}

	private void skipWhiteSpaces() {
		while (!isAtEnd() && Character.isWhitespace(mSource.charAt(mIndex))) {
			nextChar();
		}
	}

	public boolean hasNext() {
		skipWhiteSpaces();

		return !isAtEnd();
	}

	public Token next() throws ParsingException {
		if (!hasNext()) {
			throw new ParsingException("EOF reached");
		}

		final char c = mSource.charAt(mIndex);
		tokenStart();

		switch (c) {
		case '.':
			if (isCharAtOffset(1, '.')) {
				nextChar();
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
			nextChar();
			return TokenConstants.getSyntaxToken(tokenString(), tokenPosition());
		case '"':
		case '\'':
			return parseString();
		case '-':
			if (isCharAtOffset(1, '>')) {
				advance(2);

				return TokenConstants.getSyntaxToken(tokenString(), tokenPosition());
			}

			return parseOneOrEqual();
		case '+':
			// handle ++ and --
			if (isCharAtOffset(1, c)) {
				advance(2);

				return TokenConstants.getOperationToken(tokenString(), tokenPosition());
			}

			return parseOneOrEqual();
		case '/':
			if (isCharAtOffset(1, '/')) {
				return parseSingleLineComment();
			}

			if (isCharAtOffset(1, '*')) {
				return parseMultiLineComment();
			}

			return parseOneOrEqual();
		case '*':
		case '%':
		case '^':
		case '~':
			return parseOneOrEqual();
		case '!':
			return parseRefEqual();
		case '=':
			if (isCharAtOffset(1, '>')) {
				advance(2);

				return TokenConstants.getSyntaxToken(tokenString(), tokenPosition());
			}

			return parseRefEqual();
		case '&':
		case '|':
		case '>':
		case '<':
			return parseTwoOrEqual();
		}

		if (Character.isDigit(c)) {
			return parseNumber();
		}

		return parseIdentifier();
	}

	public void revert(final Token aToken) {
		mIndex = aToken.getPosition().getStart();
		mLine = aToken.getPosition().getLine();
		mChar = aToken.getPosition().getChar();
	}

	public Token parseRegExp() throws ParsingException {
		AssertUtil.runtimeAssert(nextChar() == '/');

		while (true) {
			if (isAtEnd()) {
				throw $("EOF reached before closing RegExp");
			}

			final char c = nextChar();

			if (c == '/') {
				break;
			}

			if (c == '\n') {
				throw $("New line reached before closing RegExp");
			}

			if (c == '\\') {
				if (!isAtEnd()) {
					// skip the escape
					if (nextChar() == '\n') {
						throw $("New line reached before closing RegExp");
					}
				}
			}
		}

		final String body = mSource.substring(mTokenIndex + 1, mIndex);

		int flags = 0;
		while (!isAtEnd()) {
			final char c = nextChar();

			if (!Character.isLetterOrDigit(c)) {
				break;
			}

			final Flag flag = Flag.find(c);
			if (flag == null) {
				throw $("Invalid RegExp flag: '{}'", Character.valueOf(c));
			}

			flags |= flag.bitValue();
		}

		final RegExpInfo rxi;
		try {
			rxi = new RegExpInfo(body, flags);
		} catch (final PatternSyntaxException e) {
			throw $("Inavlid pattern syntax");
		}

		return new Token(TokenType.REG_EXP, rxi, tokenPosition());
	}

	private Token parseString() throws ParsingException {
		final char stopChar = mSource.charAt(mIndex);

		nextChar();
		int lastIndex = mIndex;
		final StringBuilder retval = new StringBuilder();
		do {
			if (isAtEnd()) {
				throw $("No closing quote found");
			}

			final char c = nextChar();
			if (c == stopChar) {
				break;
			}

			if (c == '\\') {
				retval.append(mSource.substring(lastIndex, mIndex - 1));

				// if at end will fail on the next iteration
				if (!isAtEnd()) {
					lastIndex = mIndex;

					final char e = nextChar();
					if ((e == '$') || (e == '(')) {
						// TODO: interpolation
						// "\(expr)" && "\$identifier"
					}

					// TODO: handle different escapes here
				}
			}
		} while (true);

		retval.append(mSource.substring(lastIndex, mIndex - 1));
		return new Token(TokenType.CONSTANT, retval.toString(), tokenPosition());
	}

	public Token parseInterpolationPart() throws ParsingException {
		return null;
	}

	private Token parseNumber() throws ParsingException {
		int base = 0; // 0 will be normalized to 10, used to help parsing floats
		if (isCharAtOffset(0, '0') && !isAtEnd(1)) {
			final char next = Character.toLowerCase(mSource.charAt(mIndex + 1));
			final Integer radix = RADIX_MAP.get(Character.valueOf(next));

			if (radix != null) {
				base = radix.intValue();

				advance(2);
			} else {
				if (Character.isDigit(next)) {
					// don't allow mistakes to pass through
					final String radixes = ArrayUtil.arrayJoin("/", RADIX_MAP.keySet().toArray());

					throw $("Numeral system code expected ({})", radixes);
				}
			}
		}

		do {
			nextChar();
		} while (!isAtEnd() && isHexDigit(mSource.charAt(mIndex)));

		boolean isFloat = false;
		if (isCharAtOffset(0, '.')) {
			if (isAtEnd(1) || Character.isDigit(mSource.charAt(mIndex + 1))) {
				if (base != 0) {
					throw $("Invalid floating point syntax (cannot have numeral system specifiers)");
				}

				isFloat = true;
				do {
					nextChar();
				} while (!isAtEnd() && Character.isDigit(mSource.charAt(mIndex)));
			}
		}

		final Object constant;
		final String number = mSource.substring(mTokenIndex + ((base != 0) ? 2 : 0), mIndex);

		try {
			if (isFloat) {
				constant = Double.valueOf(number);
			} else {
				base = (base == 0) ? 10 : base;
				constant = Integer.valueOf(number, base);
			}
		} catch (final NumberFormatException e) {
			throw $("Connot be parsed into a number: {}", tokenString());
		}

		return new Token(TokenType.CONSTANT, constant, tokenPosition());
	}

	private boolean isHexDigit(final char aChar) {
		return Character.isDigit(aChar) || ((aChar >= 'a') && (aChar <= 'f')) || ((aChar >= 'A') && (aChar <= 'F'));
	}

	private boolean isIdentifierChar(final char aChar) {
		return Character.isLetterOrDigit(aChar) || (aChar == '_');
	}

	private Token parseIdentifier() throws ParsingException {
		final char firstChar = mSource.charAt(mIndex);
		if (!(Character.isLetter(firstChar) || (firstChar == '_'))) {
			throw $("Identifier starting character expected but found: {}", Character.valueOf(firstChar));
		}

		do {
			nextChar();
		} while (!isAtEnd() && isIdentifierChar(mSource.charAt(mIndex)));

		final String identifier = tokenString();
		final SourcePosition position = tokenPosition();
		final Token keyword = TokenConstants.getKeywordToken(identifier, position);

		if (keyword != null) {
			return keyword;
		}

		return new Token(TokenType.IDENTIFIER, identifier, position);
	}

	private boolean isCharAtOffset(final int aOffset, final char aChar) {
		return !isAtEnd(aOffset) && (mSource.charAt(mIndex + aOffset) == aChar);
	}

	private char nextChar() {
		char retval = mSource.charAt(mIndex);

		++mIndex;
		++mChar;

		if (retval == '\r') {
			if (isCharAtOffset(1, '\n')) {
				++mIndex;
			}

			retval = '\n';
		}

		if (retval == '\n') {
			++mLine;
			mChar = 0;
		}

		return retval;
	}

	private void advance(final int aCount) {
		for (int i = 0; i < aCount; ++i) {
			nextChar();
		}
	}

	private boolean isAtEnd() {
		return isAtEnd(0);
	}

	private boolean isAtEnd(final int aOffset) {
		return (mIndex + aOffset) >= mSource.length();
	}

	private Token parseOneOrEqual() {
		advance(isCharAtOffset(1, '=') ? 2 : 1);

		return TokenConstants.getOperationToken(tokenString(), tokenPosition());
	}

	private Token parseTwoOrEqual() {
		if (isCharAtOffset(1, mSource.charAt(mIndex))) {
			advance(isCharAtOffset(2, '=') ? 3 : 2);

			return TokenConstants.getOperationToken(tokenString(), tokenPosition());
		}

		return parseOneOrEqual();
	}

	private Token parseRefEqual() {
		if (isCharAtOffset(2, '=') && isCharAtOffset(1, '=')) {
			advance(3);

			return TokenConstants.getOperationToken(tokenString(), tokenPosition());
		}

		return parseOneOrEqual();
	}

	private Token parseSingleLineComment() {
		AssertUtil.runtimeAssert(nextChar() == '/');
		AssertUtil.runtimeAssert(nextChar() == '/');

		while (!isAtEnd()) {
			final char c = nextChar();

			if (c == '\n') {
				break;
			}
		}

		return new Token(TokenType.SL_COMMENT, tokenString(), tokenPosition());
	}

	private Token parseMultiLineComment() throws ParsingException {
		AssertUtil.runtimeAssert(nextChar() == '/');
		AssertUtil.runtimeAssert(nextChar() == '*');

		while (true) {
			if (isAtEnd()) {
				throw $("EOF reached before closing multiline comment");
			}

			final char c = nextChar();
			if ((c == '*') && isCharAtOffset(1, '/')) {
				nextChar();
				break;
			}
		}

		return new Token(TokenType.ML_COMMENT, tokenString(), tokenPosition());
	}

	private void tokenStart() {
		mTokenLine = mLine;
		mTokenChar = mChar;
		mTokenIndex = mIndex;
	}

	private SourcePosition tokenPosition() {
		return new SourcePosition(mTokenIndex, mIndex, mTokenLine, mTokenChar);
	}

	private String tokenString() {
		return mSource.substring(mTokenIndex, mIndex);
	}

	private ParsingException $(final String aMessage, final Object... aArgs) {
		final String message = StringUtil.format(aMessage, aArgs);

		@SuppressWarnings("boxing")
		final String fullMsg = StringUtil.format("{}:{} {}", mTokenLine, mTokenChar, message);

		return new ParsingException(fullMsg);
	}
}
