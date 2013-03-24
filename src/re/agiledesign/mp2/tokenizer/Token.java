package re.agiledesign.mp2.tokenizer;

public class Token {
	private final TokenType mType;
	private final Object mValue;
	private final int mPosition;

	public Token(final TokenType aType, final Object aValue, final int aLineNo, final int aCharNo) {
		mType = aType;
		mValue = aValue;
		mPosition = (aLineNo << 16) | (aCharNo & 0x0000FFFF);
	}

	public TokenType getType() {
		return mType;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) mValue;
	}

	public String getStringValue() {
		return (String) mValue;
	}

	public int getLine() {
		return mPosition >>> 16;
	}

	public int getCharNo() {
		return mPosition & 0x0000FFFF;
	}

	public String toString() {
		return "Token [Position=" + getLine() + ':' + getCharNo() + ", Type=" + mType + ", Value=" + mValue + "]";
	}
}
