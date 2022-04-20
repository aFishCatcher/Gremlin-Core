package dml.gremlin.myThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Task implements Callable<TaskList>{
	private final int blockSize = Global.blockSize;
	
	private final TaskDataBuffer dataBuffer;
	private final WorkerNode worker;
	
	public Task(TaskDataBuffer dataBuffer, WorkerNode worker) {
		this.dataBuffer = dataBuffer;
		this.worker = worker;
	}
	
	@Override
	public TaskList call() {
		long t1 = System.currentTimeMillis();
		//处理数据, 结果放在临时缓冲区
		TaskDataBuffer tempResult = worker.work(dataBuffer);
		long t2 = System.currentTimeMillis();
		
		tempResult = worker.work(dataBuffer);
		long t3 = System.currentTimeMillis();

		//将临时缓冲区合并到输出缓冲区，并尝试取出待处理数据块
		DataList blocks = worker.mergeAndSplit(tempResult, this.blockSize);
		long t4 = System.currentTimeMillis();
		System.out.println("Node: "+worker.getNum()+", curNum: "+dataBuffer.getCurNum()+ ", ThreadNum: "+Thread.currentThread().getId()+
				", first work cost: "+ (t2-t1)+", second work cost: "+(t3-t2)+", merge cost: "+(t4-t3));
		
		//construct new task
		TaskList newTasks = new TaskList();
		if(blocks != null) {
			for(int i=0; i<blocks.size(); i++) {
				newTasks.add(new Task((TaskDataBuffer)(blocks.get(i)), worker.nextNode()));
			}
		}
		newTasks.setEnd(blocks.isEnd());
//		if(shutDown) {
//			
//			Global.endTimer();
//			System.out.println("MyThreadPool time consume: "+Global.getPassTime());
//			Global.showStepOutputBuffer(worker.getStepOutputBuffer());
//			Global.exec.shutdown();
//		}
		return newTasks;
	}

}