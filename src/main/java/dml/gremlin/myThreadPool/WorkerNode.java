package dml.gremlin.myThreadPool;

import java.util.List;

public class WorkerNode<S,E> {
	private final int num;
	private final WorkerNode<S,E> nextNode;
	private final StepOutputBuffer<E> stepOutputBuffer;
	private final Worker<TaskDataBuffer<S>,TaskDataBuffer<E>> worker;  //here, workers are steps
	
	public WorkerNode(Worker<TaskDataBuffer<S>,TaskDataBuffer<E>> worker, StepOutputBuffer<E> stepOutputBuffer, WorkerNode<S,E> nextNode, int num) {
		this.worker = worker;
		this.stepOutputBuffer = stepOutputBuffer;
		this.nextNode = nextNode;
		this.num = num;
	}
	
	public int getNum() {
		return this.num;
	}

	public WorkerNode<S,E> nextNode() {
		return nextNode;
	}
	
	public boolean isEndWorker() {
		return nextNode == null;
	}
	
	public TaskDataBuffer<E> work(TaskDataBuffer<S> in) {
		TaskDataBuffer<E> out = worker.work(in);
		return out;
	}
	
	public StepOutputBuffer<E> getStepOutputBuffer() {
		return this.stepOutputBuffer;
	}
	
	/*
	 * if source node is the end node, we should put temple result
	 * into output buffer for later usage.
	 */
	public void mergeForSourceNode(TaskDataBuffer<E> tempResult) {
		this.stepOutputBuffer.merge(tempResult);
	}
	
	public DataList<E> mergeAndSplit(TaskDataBuffer<E> tempResult, int blockSize) {
		boolean isEnd = false;
		List<TaskDataBuffer<E>> blocks = null;
		
		if(!isEndWorker()) {
			synchronized(stepOutputBuffer) {
				isEnd = this.stepOutputBuffer.merge(tempResult);
				blocks = stepOutputBuffer.split(blockSize, isEnd);
			}
		}else { //if we are dealing with end node, we don't need to split out data to construct new tasks
			synchronized(stepOutputBuffer) {
				isEnd = this.stepOutputBuffer.merge(tempResult);
				blocks = null;
			}
		}
		
		DataList<E> result = new DataList<E>();
		if(blocks != null) {
			for(TaskDataBuffer<E> block:blocks) {
				result.add(block);		
			}
		}
		
		result.setEnd(isEnd && this.isEndWorker());
		return result;
	}
}
