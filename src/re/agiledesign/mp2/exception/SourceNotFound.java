package re.agiledesign.mp2.exception;

public class SourceNotFound extends SourceException {
	public SourceNotFound(final String aFileName) {
		super("Source '" + aFileName + "' not found");
	}
}
