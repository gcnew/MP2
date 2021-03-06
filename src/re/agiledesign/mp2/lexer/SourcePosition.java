package re.agiledesign.mp2.lexer;

public class SourcePosition {
	public static final SourcePosition UNKNOWN = new SourcePosition(0, 0, 0);

	private final int mStart;
	private final int mEnd;
	private final int mLineChar;

	public SourcePosition(final int aStart, final int aEnd, final int aLineChar) {
		mStart = aStart;
		mEnd = aEnd;
		mLineChar = aLineChar;
	}

	public SourcePosition(final int aStart, final int aEnd, final int aLine, final int aChar) {
		this(aStart, aEnd, (aLine << 16) | (aChar & 0x0000FFFF));
	}

	public int getStart() {
		return mStart;
	}

	public int getEnd() {
		return mEnd;
	}

	public int getLine() {
		return mLineChar >>> 16;
	}

	public int getChar() {
		return mLineChar & 0x0000FFFF;
	}

	public String toString() {
		return getLine() + ":" + getChar();
	}
}
