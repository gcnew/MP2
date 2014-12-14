package re.agiledesign.mp2.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.util.AssertUtil;

public class TokensBuffer {
	private int mIndex = 0;
	private final List<Token> mTokens;

	public TokensBuffer(final Tokenizer aTokenizer) throws ParsingException {
		mTokens = new ArrayList<Token>();

		while (aTokenizer.hasNext()) {
			mTokens.add(aTokenizer.next());
		}
	}

	public int getIndex() {
		return mIndex;
	}

	public void restoreIndex(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= mTokens.size())) {
			throw new IndexOutOfBoundsException("Inappropriate index: " + aIndex);
		}

		mIndex = aIndex;
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

	public TokenIterator tokenIterator() {
		return new TokenIteratorImpl(mTokens);
	}

	private static class TokenIteratorImpl implements TokenIterator {
		private int mIndex;
		private final List<Token> mTokens;

		public TokenIteratorImpl(final List<Token> aTokens) {
			mTokens = aTokens;
		}

		public Token current() {
			if (atEnd()) {
				throw new NoSuchElementException();
			}

			return mTokens.get(mIndex);
		}

		public boolean atEnd() {
			return mIndex == mTokens.size();
		}

		public void advance() {
			if (!atEnd()) {
				++mIndex;
			}
		}

		public TokenIterator diverge() {
			try {
				return (TokenIterator) this.clone();
			} catch (final CloneNotSupportedException e) {
				throw AssertUtil.never();
			}
		}

		public Token last() {
			if (!atEnd() || mTokens.isEmpty()) {
				throw new IllegalStateException();
			}

			return mTokens.get(mTokens.size() - 1);
		}
	}
}
