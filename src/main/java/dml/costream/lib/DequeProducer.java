package dml.costream.lib;

public class DequeProducer<E> {
	private ArrayDequeBuffer<E> proBuffer;
	
	public DequeProducer() {
		this.proBuffer = null;
	}
	
	public DequeProducer(ArrayDequeBuffer<E> proBuffer) {
		this.proBuffer = proBuffer;
	}
	
	public void setProBuffer(ArrayDequeBuffer<E> proBuffer) {
		this.proBuffer = proBuffer;
	}
	
	public ArrayDequeBuffer<E> getProBuffer() {
		return this.proBuffer;
	}
	
	// 功能
	public boolean offerElement(E e) {
		return this.proBuffer.offerElement(e);
	}
	
	public boolean offerCopySize(int copysize) {
		return this.proBuffer.offerCopySize(copysize);
	}
}
