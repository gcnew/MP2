package re.agiledesign.mp2.internal.sourceprovider;

import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.exception.SourceException;
import re.agiledesign.mp2.exception.SourceNotFound;

public class StringSourceProvider implements SourceProvider {
	private final Map<String, String> mMappings;

	public StringSourceProvider(final Map<String, String> aMappings) {
		mMappings = aMappings;
	}

	public StringSourceProvider(final String[][] aPathToSource) {
		mMappings = new HashMap<String, String>((int) (aPathToSource.length * 1.5));

		for (final String[] pathToSource : aPathToSource) {
			mMappings.put(pathToSource[0], pathToSource[1]);
		}
	}

	public String resolve(final String aPath) {
		return aPath;
	}

	public String getSource(final String aResolvedPath) throws SourceException {
		if (!mMappings.containsKey(aResolvedPath)) {
			throw new SourceNotFound(aResolvedPath);
		}

		return mMappings.get(aResolvedPath);
	}
}
