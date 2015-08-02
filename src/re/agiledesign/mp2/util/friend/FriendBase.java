package re.agiledesign.mp2.util.friend;

import re.agiledesign.mp2.util.Util;

public abstract class FriendBase {
	public FriendBase() {
		final Friends friends = getClass().getAnnotation(Friends.class);
		final Class<?> caller = Util.getCaller(getClass());

		for (final Class<?> friend : friends.value()) {
			if (friend == caller) {
				return;
			}
		}

		throw new NotAFriendException(caller, getClass());
	}
}
