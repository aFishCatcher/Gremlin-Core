package dml.costream.lib;

import java.util.ArrayDeque;
import java.util.Queue;

public class ArrayDequeBuffer<E> {
	private Queue<E> buffer;
	private Queue<Integer> copySize;
	
	public ArrayDequeBuffer() {
		buffer = new ArrayDeque<E>();
		copySize = new ArrayDeque<Integer>();
	}
		
	public E getElement() {
		return buffer.element();
	}
	
	public int getCopySize() {
		return copySize.element();
	}
	
	public E pollElement() {
		return buffer.poll();
	}
	
	public int pollCopySize() {
		return copySize.poll();
	}
	
	public boolean offerElement(E e) {
		return buffer.offer(e);		
	}
	
	public boolean offerCopySize(int copysize) {
		return copySize.offer(copysize);
	}
}
