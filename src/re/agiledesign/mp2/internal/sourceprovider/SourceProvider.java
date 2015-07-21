package re.agiledesign.mp2.internal.sourceprovider;

import re.agiledesign.mp2.exception.SourceException;

public interface SourceProvider {
	public String resolve(final String aPath);

	public String getSource(final String aResolvedPath) throws SourceException;
}
