package dml.gremlin.myThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class StepOutputBuffer<E> extends LinkedDataBuffer<E>{
	private int expectNum = 1;
	private int nextSplitNum = 1;
	private PriorityQueue<TaskDataBuffer<E>> futureBuffers = new PriorityQueue<>((a,b)->a.getCurNum()-b.getCurNum());

	/*
	 * Merge other LinkedBuffer into this one
	 * other: LinkedBuffer to be merged with
	 * @return: if the end buffer is merged
	 * should be thread safe
	 */
	public synchronized boolean merge(TaskDataBuffer<E> other) {
		int curNum = other.getCurNum();
		
		if(this.expectNum != curNum) {
			futureBuffers.add(other);
			return false;
		}else {
			TaskDataBuffer<E> lastBuffer = other; //lastBuffer to be merged
			super.merge(lastBuffer);
			this.expectNum++;
			while(!futureBuffers.isEmpty() && futureBuffers.peek().getCurNum()==this.expectNum) {
				lastBuffer = futureBuffers.poll(); 
				super.merge(lastBuffer);
				this.expectNum++;
			}
			return lastBuffer.isEnd();
		}
		
	}
	
	/*
	 * Split out blocks of data each contain n elements
	 * until data size in buffer is less than n
	 * should be thread safe
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized List<LinkedDataBuffer<E>> split(int n, boolean isEnd){
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
		return (List)results;
	}
}
