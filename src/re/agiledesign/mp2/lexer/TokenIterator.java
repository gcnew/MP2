package re.agiledesign.mp2.lexer;

import re.agiledesign.mp2.util.iterator.StatefulIterator;

public interface TokenIterator extends StatefulIterator<Token> {
	public TokenIterator diverge();

	/**
	 * Returns the last valid item before reaching {@link #atEnd}.
	 * 
	 * @throws IllegalStateException if {@link #atEnd} is not reached
	 * or no previous item exists
	 * @throws UnsupportedOperationException if this method is not supported by
	 * the iterator
	 */
	public Token last();
}
