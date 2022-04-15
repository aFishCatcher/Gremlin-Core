package dml.gremlin.myThreadPool;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.util.FastNoSuchElementException;

public class SourceTask implements Runnable{
	private final WorkerNode worker;
	private int nextNum = 1;
	private int blockSize = Global.blockSize;
	
	public SourceTask(WorkerNode worker) {
		this.worker = worker;
	}
	
	public void run() {
		Global.startTimer();
		//normalRun();
		testRun();
	}
	
	public void testRun() {
		long t1 = System.currentTimeMillis();
		TaskDataBuffer tempResult = null;
		
		try {
			//System.out.println("current thread: "+Thread.currentThread().getId());
			tempResult = new TaskDataBuffer(nextNum++, false);
			//生成数据
			worker.work(null, tempResult);  //may throw FastNoSuchElementException here		
		}catch(FastNoSuchElementException e) {
			tempResult = new TaskDataBuffer(tempResult, nextNum-1, true);

			if(worker.isEndWorker()) {
				worker.mergeForSourceNode(tempResult);
				Global.endTimer();
				System.out.println("MyThreadPool time consume: " + Global.getPassTime());
				Global.showStepOutputBuffer(worker.getStepOutputBuffer());
				Global.exec.shutdown();
			}else {
				List<TaskDataBuffer> blocks = worker.mergeAndSplit(tempResult, this.blockSize);
				long t2 = System.currentTimeMillis();
				long time_interval = t2 -t1;
				System.out.println("ThreadNum:"+Thread.currentThread().getId()+" Source cost Time: "+time_interval);
				if(blocks != null) {
					for(int i=0; i<blocks.size(); i++) {
						Task task = new Task(blocks.get(i), worker.nextNode());
						Global.exec.submit(task);
					}
				}
			}
			
		}
	}
	
	public void normalRun() {
		TaskDataBuffer tempResult = null;
		
		try {
			while(true) {
				//System.out.println("current thread: "+Thread.currentThread().getId());
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

}
