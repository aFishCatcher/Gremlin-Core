package dml.stream.util;

public interface Producer<E> {
	public boolean offerElement(E e);
	
	public boolean offerCopySize(int copysize);
	
	public int getSize();
}
