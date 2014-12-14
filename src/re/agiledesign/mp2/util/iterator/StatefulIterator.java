package re.agiledesign.mp2.util.iterator;

public interface StatefulIterator<T> {
	public T current();

	public boolean atEnd();

	public void advance();
}
