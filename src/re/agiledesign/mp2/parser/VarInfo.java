package re.agiledesign.mp2.parser;

public class VarInfo {
	public enum Visibility {
		LOCAL, GLOBAL, VAR(LOCAL), CAPTURE(LOCAL), ARGUMENT;

		private final Visibility mStore;

		private Visibility(final Visibility aStore) {
			mStore = aStore;
		}

		private Visibility() {
			this(null);
		}

		public Visibility getStore() {
			return (mStore != null) ? mStore : this;
		}
	}

	private final int mIndex;
	private final String mVarName;
	private final Visibility mVisibility;
	private final LexicalScope mScope;

	private final VarInfo mCaptured;

	private boolean mAssigned;

	public VarInfo(final String aVarName, final Visibility aVisibility, final LexicalScope aScope, final int aIndex) {
		this(aVarName, aVisibility, aScope, aIndex, null);
	}

	public VarInfo(
		final String aVarName,
		final Visibility aVisibility,
		final LexicalScope aScope,
		final int aIndex,
		final VarInfo aCapture
	) {
		mVarName = aVarName;
		mVisibility = aVisibility;
		mScope = aScope;
		mIndex = aIndex;
		mCaptured = aCapture;

		if (aVisibility == Visibility.ARGUMENT) {
			assign();
		}
	}

	public String getVarName() {
		return mVarName;
	}

	public boolean isCapture() {
		return mVisibility == Visibility.CAPTURE;
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

	public VarInfo getCaptured() {
		return mCaptured;
	}
}
