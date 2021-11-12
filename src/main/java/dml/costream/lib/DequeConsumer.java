package dml.costream.lib;

import java.util.ArrayDeque;

public class DequeConsumer<E> {
	private ArrayDequeBuffer<E> conBuffer;
	
	public DequeConsumer() {
		this.conBuffer = null;
	}
	
	public DequeConsumer(ArrayDequeBuffer<E> conBuffer) {
		this.conBuffer = conBuffer;
	}
	
	public void setConBuffer(ArrayDequeBuffer<E> conBuffer) {
		this.conBuffer = conBuffer;
	}
	
	public ArrayDequeBuffer<E> getConBuffer() {
		return this.conBuffer;
	}
	
	public E getElement() {
		return this.conBuffer.getElement();
	}
	
	public E pollElement() {
		return this.conBuffer.pollElement();
	}
	
	public int getCopySize() {
		return this.conBuffer.getCopySize();
	}
	
	public int pollCopySize() {
		return this.conBuffer.pollCopySize();
	}
}
