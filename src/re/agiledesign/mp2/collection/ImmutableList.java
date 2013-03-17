package re.agiledesign.mp2.collection;

import java.util.List;

public interface ImmutableList<T> extends List<T> {
	public ImmutableList<T> subList2(final int aFrom, final int aTo);
}
