package re.agiledesign.mp2.util.friend;

public class NotAFriendException extends RuntimeException {
	public NotAFriendException(final Class<?> aPretender, final Class<?> aAffected) {
		super(aPretender + " is not a friend of " + aAffected);
	}
}
