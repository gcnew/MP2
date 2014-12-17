package re.agiledesign.mp2.lexer;

import java.util.regex.Pattern;

public class RegExpInfo {
	public enum Flag {
		GLOBAL('g'), CASE_INSENSITIVE('i'), MULTILINE('m');

		private final char mSign;

		private Flag(final char aSign) {
			mSign = aSign;
		}

		public char sign() {
			return mSign;
		}

		public int bitValue() {
			return 1 << ordinal();
		}

		public static Flag find(final char c) {
			for (final Flag f : values()) {
				if (f.sign() == c) {
					return f;
				}
			}

			return null;
		}
	}

	private final int mFlags;
	private final Pattern mPattern;

	public RegExpInfo(final String aBody, final int aFlags) {
		mFlags = aFlags;

		int javaFlags = 0;
		javaFlags |= isMultiline() ? Pattern.MULTILINE : 0;
		javaFlags |= isInsensitive() ? Pattern.CASE_INSENSITIVE : 0;

		mPattern = Pattern.compile(aBody, javaFlags);
	}

	public Pattern getPattern() {
		return mPattern;
	}

	public boolean isGlobal() {
		return (mFlags & Flag.GLOBAL.bitValue()) != 0;
	}

	public boolean isMultiline() {
		return (mFlags & Flag.MULTILINE.bitValue()) != 0;
	}

	public boolean isInsensitive() {
		return (mFlags & Flag.CASE_INSENSITIVE.bitValue()) != 0;
	}
}
