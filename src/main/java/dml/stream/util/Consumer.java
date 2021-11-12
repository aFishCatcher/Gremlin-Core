package dml.stream.util;

public interface Consumer<E> {
	public E getElement();
	
	public E pollElement();
	
	public int getCopySize();
	
	public int pollCopySize();
	
	public int getSize();
}
