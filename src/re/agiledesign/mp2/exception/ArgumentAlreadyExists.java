package re.agiledesign.mp2.exception;

public class ArgumentAlreadyExists extends ParsingException {
	public ArgumentAlreadyExists(String aArgName) {
		super("Argument with name '" + aArgName + "' already exists");
	}
}
