package re.agiledesign.mp2.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static String FORMAT_REGEXP = "\\{(\\d*)\\}";
	private static Pattern FORMAT_PATTERN = Pattern.compile(FORMAT_REGEXP);

	public static String replace(final CharSequence aString,
			final String aRegexp,
			final Func<CharSequence, MatchResult> aFunction) {
		return replace(aString, Pattern.compile(aRegexp), aFunction);
	}

	public static String replace(final CharSequence aString,
			final Pattern aPattern,
			final Func<CharSequence, MatchResult> aFunction) {
		final Matcher m = aPattern.matcher(aString);

		if (!m.find()) {
			return aString.toString();
		}

		int lastFoundIdx = 0;
		final StringBuilder retval = new StringBuilder();
		do {
			retval.append(aString.subSequence(lastFoundIdx, m.start()));
			retval.append(aFunction.invoke(m.toMatchResult()));
			lastFoundIdx = m.end();
		} while (m.find());

		retval.append(aString.subSequence(lastFoundIdx, aString.length()));
		return retval.toString();
	}

	public static String format(final String aFormat, final Object... aArgs) {
		return replace(aFormat, FORMAT_PATTERN, new FormatFunc(aArgs));
	}

	private static class FormatFunc implements Func<CharSequence, MatchResult> {
		private int mIdx;
		private final Object[] mArgs;

		public FormatFunc(final Object[] aArgs) {
			mArgs = aArgs;
		}

		public CharSequence invoke(final MatchResult aMatchResult) {
			final String captured = aMatchResult.group(1);
			final int idx = captured.isEmpty() ? mIdx++ : Integer.parseInt(captured);

			return (idx < mArgs.length) ? String.valueOf(mArgs[idx]) : null;
		}
	}
}
