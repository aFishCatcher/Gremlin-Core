package dml.gremlin.myThreadPool;

import java.util.Iterator;

public class TaskDataBuffer<E> extends LinkedDataBuffer<E>{
	private final int curNum;
	private final boolean isEnd;

	public TaskDataBuffer(int curNum, boolean isEnd){
		super();
		this.curNum = curNum;
		this.isEnd = isEnd;
	}
	
	public TaskDataBuffer(LinkedDataBuffer<E> other, int curNum, boolean isEnd){
		super(other);
		this.curNum = curNum;
		this.isEnd = isEnd;
	}
	
	public TaskDataBuffer(TaskDataBuffer<E> other){
		super(other);
		this.curNum = other.curNum;
		this.isEnd = other.isEnd;
	}
	
	/*
	 * Appends the specified element to the end of this list
	 * e: element to be appended to this list
	 */
	public void add(E e) {
		super.add(e);
	}
	
	public Iterator<E> iterator(){
		return super.iterator();
	}
	
	public boolean isEnd() {
		return isEnd;
	}

	public int getCurNum() {
		return curNum;
	}
}
