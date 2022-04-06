package dml.gremlin.myThreadPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StepOutputBuffer<E> extends LinkedDataBuffer<E>{
	private int expectNum = 1;
	private int nextSplitNum = 1;

	public synchronized void checkNum(int curNum) {
		while(expectNum != curNum){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Merge other LinkedBuffer into this one
	 * other: LinkedBuffer to be merged with
	 * should be thread safe
	 */
	public synchronized void merge(TaskDataBuffer<E> other) {
		super.merge(other);
		expectNum++;
	}
	
	/*
	 * Split out blocks of data each contain n elements
	 * until data size in buffer is less than n
	 * should be thread safe
	 */
	@Override
	public synchronized List split(int n, boolean isEnd){
		List<LinkedDataBuffer<E>> blockList  = super.split(n, isEnd);
		if(blockList==null) return null;
		
		//convert LinkedDataBuffer to TaskDataBuffer
		List<TaskDataBuffer<E>> results = new ArrayList<>();
		int len = blockList.size();
		for(int i=0; i<len-1; i++) {
			TaskDataBuffer<E> dataBuffer = new TaskDataBuffer<>(blockList.get(i),nextSplitNum++, false);
			results.add(dataBuffer);
		}
		TaskDataBuffer<E> dataBuffer = new TaskDataBuffer<>(blockList.get(len-1),nextSplitNum++, isEnd);
		results.add(dataBuffer);
		return results;
	}
}
