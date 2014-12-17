package re.agiledesign.mp2.lexer;

import java.util.ArrayList;
import java.util.List;

import re.agiledesign.mp2.exception.ParsingException;

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
}
