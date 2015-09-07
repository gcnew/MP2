package re.agiledesign.mp2.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import re.agiledesign.mp2.collection.ImmutableArrayList;

public class Util {
	public static <T> String getClassString(final T aObject) {
		return (aObject != null) ? aObject.getClass().toString() : "(null)";
	}

	@SuppressWarnings("unchecked")
	public static <T extends Exception> T rethrow(final Exception aException) throws T {
		throw (T) aException;
	}

	public static RuntimeException rethrowUnchecked(final Exception aException) {
		Util.<RuntimeException> rethrow(aException);

		throw new AssertionError("### Error: this code should be unreachable ###");
	}

	public static <T> List<T> immutableList(final List<T> aList) {
		if (aList.isEmpty()) {
			return Collections.emptyList();
		}

		return new ImmutableArrayList<T>(aList);
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast(final Object aObject) {
		return (T) aObject;
	}

	public static Class<?> getCaller(final Class<?> aSelf) {
		try {
			final String selfName = aSelf.getName();
			final StackTraceElement[] elements = new Exception().getStackTrace();

			int i;
			for (i = 0; i < elements.length; ++i) {
				if (elements[i].getClassName().equals(selfName)) {
					break;
				}
			}

			return Class.forName(elements[i + 1].getClassName());
		} catch (final ClassNotFoundException e) {
			throw AssertUtil.<RuntimeException> never();
		}
	}

	@SuppressWarnings("resource")
	public static String readFile(final String aFileName) throws IOException {
		InputStream fs = null;
		try {
			fs = new FileInputStream(aFileName);

			return readStream(fs);
		} finally {
			closeQuietly(fs);
		}
	}

	public static String readStream(final InputStream aStream) {
		final Scanner s = new Scanner(aStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static void closeQuietly(final InputStream aStream) {
		try {
			if (aStream != null) {
				aStream.close();
			}
		} catch (final IOException ignored) {
		}
	}
}
