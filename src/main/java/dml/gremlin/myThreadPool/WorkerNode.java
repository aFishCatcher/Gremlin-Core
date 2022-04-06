package dml.gremlin.myThreadPool;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public class WorkerNode {
	
	private final WorkerNode nextNode;
	private final StepOutputBuffer stepOutputBuffer;
	private Worker<TaskDataBuffer, TaskDataBuffer> worker;  //here, workers are steps
	
	public WorkerNode(Worker worker, StepOutputBuffer stepOutputBuffer, WorkerNode nextNode) {
		this.worker = worker;
		this.stepOutputBuffer = stepOutputBuffer;
		this.nextNode = nextNode;
	}
	
	public WorkerNode nextNode() {
		return nextNode;
	}
	
	public boolean isEndWorker() {
		return nextNode == null;
	}
	
	public void work(TaskDataBuffer in, TaskDataBuffer out) {
		worker.work(in, out);
	}
	
	public StepOutputBuffer getStepOutputBuffer() {
		return this.stepOutputBuffer;
	}
	
	/*
	 * if source node is the end node, we should put temple result
	 * into output buffer for later usage.
	 */
	public void mergeForSourceNode(TaskDataBuffer tempResult) {
		this.stepOutputBuffer.merge(tempResult);
	}
	
	public List<TaskDataBuffer> mergeAndSplit(TaskDataBuffer tempResult, int blockSize) {
		int curNum = tempResult.getCurNum();
		boolean isEnd = tempResult.isEnd();
		List<TaskDataBuffer> blocks = null;
		
		if(!isEndWorker()) {
			synchronized(stepOutputBuffer) {
				this.stepOutputBuffer.checkNum(curNum);  //thread may wait here, in checkNum()
				this.stepOutputBuffer.merge(tempResult);
				blocks = stepOutputBuffer.split(blockSize, isEnd);
				this.stepOutputBuffer.notifyAll();
			}
		}else { //if we are dealing with end node, we don't need to split out data to construct new tasks
			synchronized(stepOutputBuffer) {
				this.stepOutputBuffer.checkNum(curNum);
				this.stepOutputBuffer.merge(tempResult);
				this.stepOutputBuffer.notifyAll();
			}
		}
		
		if(isEndWorker() && isEnd) {
			showStepOutputBuffer();
		}
		return blocks;
	}
	
	private void showStepOutputBuffer(){
		String fileName = "output/myThreadPoolResult.txt";
		try(PrintWriter out = new PrintWriter(fileName)) {
			Iterator it = this.stepOutputBuffer.iterator();
			while(it.hasNext())
				out.println(it.next());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
