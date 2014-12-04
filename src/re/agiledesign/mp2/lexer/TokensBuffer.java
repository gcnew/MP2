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
