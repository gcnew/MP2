package re.agiledesign.mp2.internal.sourceprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

	@SuppressWarnings("resource")
	public String getSource(final String aResolvedPath) throws SourceException {
		final File f = new File(mBasePath, aResolvedPath);

		if (!f.exists()) {
			throw new SourceNotFound(aResolvedPath);
		}

		InputStream fs = null;
		try {
			fs = new FileInputStream(f);

			return Util.readStream(fs);
		} catch (FileNotFoundException e) {
			throw new SourceException("Reading source '" + aResolvedPath + "' failed", e);
		} finally {
			Util.closeQuietly(fs);
		}
	}
}
