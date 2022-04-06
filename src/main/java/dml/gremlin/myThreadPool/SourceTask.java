package dml.gremlin.myThreadPool;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.util.FastNoSuchElementException;

public class SourceTask implements Runnable{
	private final WorkerNode worker;
	private int nextNum = 1;
	
	public SourceTask(WorkerNode worker) {
		this.worker = worker;
	}
	
	@Override
	public void run() {
		TaskDataBuffer<Traverser> tempResult = null;
		
		try {
			while(true) {
				tempResult = new TaskDataBuffer<Traverser>(nextNum++, false);
				//生成数据
				worker.work(null, tempResult);  //may throw FastNoSuchElementException here
			
				//构建新的task
				if(worker.isEndWorker()) {
					worker.mergeForSourceNode(tempResult);  //put result in the output buffer
				}else {
					Task task = new Task(tempResult, worker.nextNode());
					Global.exec.execute(task);
				}
				
			}
		}catch(FastNoSuchElementException e) {
			tempResult = new TaskDataBuffer<Traverser>(tempResult, nextNum-1, true);

			if(worker.isEndWorker()) {
				worker.mergeForSourceNode(tempResult);
				Global.endTimer();
				System.out.println("MyThreadPool time consume: " + Global.getPassTime());
				Global.exec.shutdown();
			}else {
				Task task = new Task(tempResult, worker.nextNode());
				Global.exec.execute(task);
			}
			
		}
		
	}

}
