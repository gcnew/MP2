package re.agiledesign.mp2.internal.sourceprovider;

import re.agiledesign.mp2.exception.SourceException;
import re.agiledesign.mp2.exception.SourceNotFound;

public class NullProvider implements SourceProvider {
	private static final NullProvider INSTANCE = new NullProvider();

	private NullProvider() {
	}

	public String resolve(final String aPath) {
		return aPath;
	}

	public String getSource(final String aResolvedPath) throws SourceException {
		throw new SourceNotFound(aResolvedPath);
	}

	public static NullProvider instance() {
		return INSTANCE;
	}
}
