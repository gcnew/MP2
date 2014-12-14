package re.agiledesign.mp2.collection;


public class ConsListWrapper<T> extends AbstractConsList<T> {
	private final ImmutableList<T> mList;

	public ConsListWrapper(final ImmutableList<T> aList) {
		mList = aList;
	}

	public T first() {
		return mList.get(0);
	}

	public SequentialList<T> rest() {
		final ImmutableList<T> retval = mList.subList2(1, -1);

		if (retval == null) {
			return null;
		}

		if (retval instanceof SequentialList) {
			return (SequentialList<T>) retval;
		}

		return new ConsListWrapper<T>(retval);
	}

	public SequentialList<T> cons(final T aItem) {
		// TODO: if this is a sublist we are leaking
		return new ConsList<T>(aItem, this);
	}
}
