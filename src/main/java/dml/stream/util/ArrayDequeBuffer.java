package dml.stream.util;

import java.util.ArrayDeque;
import java.util.Queue;

public class ArrayDequeBuffer<E> implements Consumer<E>, Producer<E> {
	
	private Queue<E> buffer;
	private Queue<Integer> copySize;

	public ArrayDequeBuffer () {
		buffer = new ArrayDeque<E>();
		copySize = new ArrayDeque<Integer>();
	}
	
	@Override
	public boolean offerElement(E e) {
		return buffer.offer(e);	
	}

	@Override
	public boolean offerCopySize(int copysize) {
		return copySize.offer(copysize);
	}

	@Override
	public E getElement() {
		return buffer.element();
	}

	@Override
	public E pollElement() {
		return buffer.poll();
	}

	@Override
	public int getCopySize() {
		return copySize.element();
	}

	@Override
	public int pollCopySize() {
		return copySize.poll();
	}

	@Override
	public int getSize() {
		return buffer.size();
	}
}
