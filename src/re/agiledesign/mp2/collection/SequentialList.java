package re.agiledesign.mp2.collection;

public interface SequentialList<T> extends ImmutableList<T> {
	public T first();

	public SequentialList<T> rest();

	public SequentialList<T> subList2(int aFrom, int aTo);
}
