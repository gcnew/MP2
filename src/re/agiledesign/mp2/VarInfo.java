package re.agiledesign.mp2;

public class VarInfo {
	public enum Visibility {
		LOCAL, GLOBAL, VAR, CLOSED, ARGUMENT
	}

	private final int mIndex;
	private final String mVarName;
	private final Visibility mVisibility;
	private final LexicalScope mScope;

	private boolean mAssigned;

	public VarInfo(final String aVarName, final Visibility aVisibility, final LexicalScope aScope, final int aIndex) {
		mVarName = aVarName;
		mVisibility = aVisibility;
		mScope = aScope;
		mIndex = aIndex;

		if (aVisibility == Visibility.ARGUMENT) {
			assign();
		}
	}

	public String getVarName() {
		return mVarName;
	}

	public boolean isClosed() {
		return mVisibility == Visibility.CLOSED;
	}

	public boolean isLocal() {
		return mVisibility == Visibility.LOCAL;
	}

	public boolean isGlobal() {
		return mVisibility == Visibility.GLOBAL;
	}

	public boolean isVar() {
		return mVisibility == Visibility.VAR;
	}

	public boolean isArgument() {
		return mVisibility == Visibility.ARGUMENT;
	}

	public LexicalScope getScope() {
		return mScope;
	}

	public Visibility getVisibility() {
		return mVisibility;
	}

	public boolean isAssigned() {
		return mAssigned;
	}

	public void assign() {
		mAssigned = true;
	}

	public int getIndex() {
		return mIndex;
	}
}
