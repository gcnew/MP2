package re.agiledesign.mp2.internal.statements;

import re.agiledesign.mp2.internal.Context;
import re.agiledesign.mp2.internal.Scope;

public class LoadStatement extends Statement {
	private final String mPath;

	public LoadStatement(final String aPath) {
		mPath = aPath;
	}

	public void execute(final Scope aScope) throws Exception {
		final Context context = (aScope instanceof Context) ? (Context) aScope : aScope.getPrevious();

		context.loadModule(mPath);
	}
}
