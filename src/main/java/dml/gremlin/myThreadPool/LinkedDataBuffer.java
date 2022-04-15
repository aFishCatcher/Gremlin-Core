package dml.gremlin.myThreadPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedDataBuffer<E> implements Iterable<E> {
	Node first;
	Node last;
	int size;
	
	LinkedDataBuffer(){
		this.first = null;
		this.last = null;
		this.size = 0;
	}
	
	LinkedDataBuffer(LinkedDataBuffer<E> other){
		this.first = other.first;
		this.last = other.last;
		this.size = other.size;
	}
	
	/*
	 * add an element to this buffer
	 * @param data: the element to add
	 */
	public void add(E data) {
		final Node l = last;
        final Node newNode = new Node(data, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
	}

	/*
	 * get an iterator to look through all elements
	 */
	@Override
	public Iterator<E> iterator() {
		return new myIterator();
	}
	
	/*
	 * merge the other buffer into this one
	 */
	public void merge(LinkedDataBuffer<E> other) {
		if(other == null || other.size == 0) return;
		if(this.size==0) {
			this.first = other.first;
			this.last = other.last;
			this.size = other.size;
		}else {
			this.last.next = other.first;
			this.last = other.last;
			this.size += other.size;
		}
	}
	
	/*
	 * get a node using index
	 */
	private Node getNode(int index) {
		assert(index >= 0 && index < this.size);
		Node node = first;
		while(index > 0) {
			node = node.next;
			index--;
		}
		return node;
	}
	
	/*
	 * Split out blocks of data each contain n elements
	 * until data size in buffer is less than n
	 */
	public List<LinkedDataBuffer<E>> split(int n, boolean isEnd){
		if(this.size < n && !isEnd) return null;
		List<LinkedDataBuffer<E>> blockList  = new ArrayList<>();
		while(this.size >= n) {
			Node splitNode = getNode(n-1);
			LinkedDataBuffer<E> block = new LinkedDataBuffer<>();
			block.first = this.first;
			block.last = splitNode;
			block.size = n;
			this.first = splitNode.next;
			splitNode.next = null;
			if(this.last==splitNode) this.last = null;
			this.size -= n;
			blockList.add(block);
		}
		//construct a block contains all left elements, or just empty
		if(isEnd) {
			LinkedDataBuffer<E> block = new LinkedDataBuffer<>();
			block.first = this.first;
			block.last = this.last;
			block.size = this.size;
			this.first = this.last = null;
			this.size = 0;
			blockList.add(block);
		}
		return blockList;
	}
	
	private class myIterator implements Iterator<E>{
		Node next = first;
		int nextIndex = 0;
		
		@Override
		public boolean hasNext() {
			return nextIndex < size;
		}

		@Override
		public E next() {
			E item = next.data;
			next = next.next;
			nextIndex++;
			return item;
		}
		
	}
	
	private class Node{
		E data;
		Node next;
		
		Node(E data, Node next){
			this.data = data;
			this.next = next;
		}
	}
	
}