package dml.gremlin.myThreadPool;

import org.apache.tinkerpop.gremlin.process.traversal.Step;

import java.util.List;

public class WorkerNode {
	
	private final WorkerNode nextNode;
	@SuppressWarnings("rawtypes")
	private final StepOutputBuffer stepOutputBuffer;
	@SuppressWarnings("rawtypes")
	private Worker<TaskDataBuffer,TaskDataBuffer> worker;  //here, workers are steps
	private boolean shutDown;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public WorkerNode(Worker<TaskDataBuffer,TaskDataBuffer> worker, StepOutputBuffer stepOutputBuffer, WorkerNode nextNode) {
		this.worker = worker;
		this.stepOutputBuffer = stepOutputBuffer;
		this.nextNode = nextNode;
		this.shutDown = false;
	}

	/*
	public WorkerNode clone(){
		WorkerNode other = new WorkerNode(this.worker.clone(), this.stepOutputBuffer, this.nextNode);
		other.shutDown = this.shutDown;
		return other;
	}


	public WorkerNode(WorkerNode other){
		this.worker = other.worker.clone();
		this.stepOutputBuffer = other.stepOutputBuffer;
		this.nextNode = other.nextNode;
		this.shutDown = other.shutDown;
	}
	*/
	public WorkerNode nextNode() {
		return nextNode;
	}
	
	public boolean isEndWorker() {
		return nextNode == null;
	}
	
	@SuppressWarnings("rawtypes")
	public void work(TaskDataBuffer in, TaskDataBuffer out) {
		worker.work(in, out);
	}
	
	@SuppressWarnings("rawtypes")
	public StepOutputBuffer getStepOutputBuffer() {
		return this.stepOutputBuffer;
	}
	
	public boolean shutDown() {
		return this.shutDown;
	}
	
	/*
	 * if source node is the end node, we should put temple result
	 * into output buffer for later usage.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void mergeForSourceNode(TaskDataBuffer tempResult) {
		this.stepOutputBuffer.merge(tempResult);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TaskDataBuffer> mergeAndSplit(TaskDataBuffer tempResult, int blockSize) {
		boolean isEnd = false;
		List<TaskDataBuffer> blocks = null;
		
		if(!isEndWorker()) {
			synchronized(stepOutputBuffer) {
				isEnd = this.stepOutputBuffer.merge(tempResult);
				blocks = stepOutputBuffer.split(blockSize, isEnd);
			}
		}else { //if we are dealing with end node, we don't need to split out data to construct new tasks
			synchronized(stepOutputBuffer) {
				isEnd = this.stepOutputBuffer.merge(tempResult);
			}
		}
		this.shutDown = isEnd && this.isEndWorker();
		return blocks;
	}
}
