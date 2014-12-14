package re.agiledesign.mp2.collection;

import java.util.List;

public interface ImmutableList<T> extends List<T> {
	/*
	public ImmutableList<T> slice(int aFrom, int aTo);

	public ImmutableList<T> splice(int aFrom, int aCount, T... aValues);

	public ImmutableList<T> splice(int aFrom, int aCount, List<? extends T> aValues);

	public ImmutableList<T> concat(List<? extends T> aOther);
	*/

	public ImmutableList<T> subList2(int aFrom, int aTo);
}
