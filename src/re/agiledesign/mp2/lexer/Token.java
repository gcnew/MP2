package re.agiledesign.mp2.lexer;

public class Token {
	private final TokenType mType;
	private final Object mValue;
	private final SourcePosition mPosition;

	public Token(final TokenType aType, final Object aValue, final SourcePosition aPosition) {
		mType = aType;
		mValue = aValue;
		mPosition = aPosition;
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

	public SourcePosition getPosition() {
		return mPosition;
	}

	public String toString() {
		return "Token [Position=" + getPosition() + ", Type=" + getType() + ", Value=" + getValue() + "]";
	}
}
