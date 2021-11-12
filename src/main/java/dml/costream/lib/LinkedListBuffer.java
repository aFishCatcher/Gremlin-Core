package dml.costream.lib;

import java.util.LinkedList;
import java.util.Queue;

public class LinkedListBuffer<E> {
	private Queue<E> buffer;
	private Queue<Integer> copySize;
	
	public LinkedListBuffer() {
		buffer = new LinkedList<E>();
		copySize = new LinkedList<Integer>();
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
