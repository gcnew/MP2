package re.agiledesign.mp2.lexer;

import java.util.ArrayList;
import java.util.List;

import re.agiledesign.mp2.collection.ConsList;
import re.agiledesign.mp2.collection.SequentialList;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.util.Pair;

public class SimpleLexer implements Lexer {
	private final String mSource;
	private final Tokenizer mTokenizer;

	private Token mLast;
	private Token mCurrent;
	private boolean mSkip;
	private List<Token> mComments;
	private SequentialList<Pair<Token, Integer>> mSavePoints;

	public SimpleLexer(final String aSource) {
		mSource = aSource;
		mTokenizer = new Tokenizer(mSource);
	}

	public Token look() throws ParsingException {
		if ((mCurrent != null) || !mTokenizer.hasNext()) {
			return mCurrent;
		}

		mCurrent = mTokenizer.next();
		if (mSkip) {
			while (Lexer.SKIP_TYPES.contains(mCurrent.getType())) {
				if (mComments == null) {
					mComments = new ArrayList<Token>();
				}

				mComments.add(mCurrent);

				if (!mTokenizer.hasNext()) {
					mCurrent = null;
					break;
				}

				mCurrent = mTokenizer.next();
			}
		}

		return mCurrent;
	}

	public boolean atEnd() {
		return (mCurrent == null) && !mTokenizer.hasNext();
	}

	public void advance() {
		advanceNoSkip();
		mSkip = true;
	}

	public void advanceNoSkip() {
		if (atEnd()) {
			throw new RuntimeException("EOF already reached");
		}

		mSkip = false;
		if (!Lexer.SKIP_TYPES.contains(mCurrent.getType())) {
			mLast = mCurrent;
		}

		mCurrent = null;
	}

	public Token parseRegExp() throws ParsingException {
		mTokenizer.revert(mCurrent);

		mSkip = false;
		mCurrent = mTokenizer.parseRegExp();

		return mCurrent;
	}

	public Token parseInterpolationPart() throws ParsingException {
		mSkip = false;
		mCurrent = mTokenizer.parseInterpolationPart();

		return mCurrent;
	}

	public List<Token> comments() {
		return mComments;
	}

	public void clearComments() {
		mComments = null;
	}

	public void save() {
		final Integer idx = (mComments != null) ? Integer.valueOf(mComments.size()) : null;

		mSavePoints = new ConsList<Pair<Token, Integer>>(new Pair<Token, Integer>(mCurrent, idx), mSavePoints);
	}

	public void reload() {
		mTokenizer.revert(mSavePoints.first().getFirst());

		final Integer idx = mSavePoints.first().getSecond();
		if (idx == null) {
			clearComments();
		} else {
			mComments.subList(idx.intValue(), mComments.size()).clear();
		}

		advanceNoSkip();
	}

	public void release() {
		mSavePoints = mSavePoints.rest();
	}

	public boolean hasSavePonts() {
		return mSavePoints != null;
	}

	public Token last() {
		if (!atEnd()) {
			throw new IllegalStateException();
		}

		return mLast;
	}
}
