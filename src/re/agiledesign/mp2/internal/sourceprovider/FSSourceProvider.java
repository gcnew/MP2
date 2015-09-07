package re.agiledesign.mp2.internal.sourceprovider;

import java.io.File;
import java.io.IOException;

import re.agiledesign.mp2.exception.SourceException;
import re.agiledesign.mp2.exception.SourceNotFound;
import re.agiledesign.mp2.util.Util;

public class FSSourceProvider implements SourceProvider {
	private final String mBasePath;

	public FSSourceProvider(final String aBasePath) {
		mBasePath = aBasePath;
	}

	public String resolve(final String aPath) {
		return aPath;
	}

	public String getSource(final String aResolvedPath) throws SourceException {
		final File f = new File(mBasePath, aResolvedPath);

		if (!f.exists()) {
			throw new SourceNotFound(aResolvedPath);
		}

		try {
			return Util.readFile(aResolvedPath);
		} catch (final IOException e) {
			throw new SourceException("Reading source '" + aResolvedPath + "' failed", e);
		}
	}
}
