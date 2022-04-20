package dml.gremlin.myThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.tinkerpop.gremlin.process.traversal.util.FastNoSuchElementException;

public class SourceTask implements Callable<TaskList>{
	private final WorkerNode worker;
	private int nextNum = 1;
	private final int blockSize = Global.blockSize;
	
	public SourceTask(WorkerNode worker) {
		this.worker = worker;
	}
	
	@SuppressWarnings("finally")
	@Override
	public TaskList call() {
		TaskDataBuffer tempResult = worker.work(null);
		TaskList newTasks = new TaskList();
		if(worker.isEndWorker()) {
			worker.mergeForSourceNode(tempResult);
			newTasks.setEnd(true);
		}else {
			List<TaskDataBuffer> blocks = worker.mergeAndSplit(tempResult, this.blockSize);
			if(blocks != null) {
				for(int i=0; i<blocks.size(); i++) {
					newTasks.add(new Task(blocks.get(i), worker.nextNode()));
				}
			}
		}
		return newTasks;
	}
	
	
	/*
	public void normalRun() {
		TaskDataBuffer tempResult = null;
		
		try {
			while(true) {
				tempResult = new TaskDataBuffer(nextNum++, false);
				//生成数据
				worker.work(null, tempResult);  //may throw FastNoSuchElementException here
			
				//构建新的task
				if(worker.isEndWorker()) {
					worker.mergeForSourceNode(tempResult);  //put result in the output buffer
				}else {
					Task task = new Task(tempResult, worker.nextNode());
					Global.exec.submit(task);
				}
				
			}
		}catch(FastNoSuchElementException e) {
			tempResult = new TaskDataBuffer(tempResult, nextNum-1, true);

			if(worker.isEndWorker()) {
				worker.mergeForSourceNode(tempResult);
				Global.endTimer();
				System.out.println("MyThreadPool time consume: " + Global.getPassTime());
				Global.showStepOutputBuffer(worker.getStepOutputBuffer());
				Global.exec.shutdown();
			}else {
				Task task = new Task(tempResult, worker.nextNode());
				Global.exec.execute(task);
			}
			
		}
		
	}
	*/
}
