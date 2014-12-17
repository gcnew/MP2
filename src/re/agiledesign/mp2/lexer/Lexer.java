package re.agiledesign.mp2.lexer;

import java.util.Arrays;
import java.util.List;

import re.agiledesign.mp2.exception.ParsingException;

public interface Lexer {
	public static final List<TokenType> SKIP_TYPES = Arrays.asList(TokenType.SL_COMMENT, TokenType.ML_COMMENT);

	public Token look() throws ParsingException;

	public boolean atEnd();

	public void advance();

	public void advanceNoSkip();

	public Token parseRegExp() throws ParsingException;

	public Token parseInterpolationPart() throws ParsingException;

	public List<Token> comments();

	public void clearComments();

	public void save();

	public void reload();

	public void release();

	public boolean hasSavePonts();

	/**
	 * Returns the last valid item before reaching {@link #atEnd}.
	 * 
	 * @throws IllegalStateException if {@link #atEnd} is not reached
	 * or no previous item exists
	 */
	public Token last();
}
