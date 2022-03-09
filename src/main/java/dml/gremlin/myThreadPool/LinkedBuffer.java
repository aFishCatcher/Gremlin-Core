package dml.gremlin.myThreadPool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LinkedBuffer<E> {
	private Queue<E> buffer = new LinkedList<E>();
	
	/*
	 * Appends the specified element to the end of this list
	 * e: element to be appended to this list
	 */
	public void add(E e) {
		buffer.add(e);
	}
	
	/*
	 * Merge other LinkedBuffer into this one
	 * other: LinkedBuffer to be merged with
	 * should be thread safe
	 */
	public synchronized void mergeWith(LinkedBuffer<E> other) {
		if(other==null) return;
		Iterator<E> it = other.iterator();
		while(it.hasNext()) {
			this.buffer.add(it.next());
		}
		other = null;
	}
	
	/*
	 * Split out first n elements as a LinkedBuffer
	 * n: the size of the separated LinkedBuffer
	 * return: separated LinkedBuffer
	 * should be thread safe
	 */
	private LinkedBuffer<E> split(int n){
		LinkedBuffer<E> res = new LinkedBuffer<>();
		while(n!=0) {
			E ele = this.buffer.poll();
			res.buffer.add(ele);
			n--;
		}
		return res;
	}
	
	/*
	 * Check if there is enough data
	 * if enough, return a pointer to the split of data
	 * else return null
	 */
	public synchronized LinkedBuffer<E> checkAndSplit(){
		if(this.buffer.size()<2) return null;
		return split(2);
	}
	
	/*
	 * return: a list iterator over the elements in this list
	 */
	public Iterator<E> iterator(){
		return buffer.iterator();
	}

	
}
