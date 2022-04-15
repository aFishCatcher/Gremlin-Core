package dml.gremlin.myThreadPool;

import java.util.List;

public class Task implements Runnable{
	private final int blockSize = Global.blockSize;
	
	@SuppressWarnings("rawtypes")
	private final TaskDataBuffer dataBuffer;
	private final WorkerNode worker;
	
	@SuppressWarnings("rawtypes")
	public Task(TaskDataBuffer dataBuffer, WorkerNode worker) {
		this.dataBuffer = dataBuffer;
		this.worker = worker;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		long t1 = System.currentTimeMillis();
		TaskDataBuffer tempResult = new TaskDataBuffer(dataBuffer.getCurNum(), dataBuffer.isEnd());
		
		//处理数据, 结果放在临时缓冲区
		long beforeWork = System.currentTimeMillis();
		worker.work(dataBuffer, tempResult);
		long afterWork = System.currentTimeMillis();

		//将临时缓冲区合并到输出缓冲区，并尝试取出待处理数据块
		List<TaskDataBuffer> blocks = worker.mergeAndSplit(tempResult, this.blockSize);
		boolean shutDown = worker.shutDown();
		
		//construct new task
		if(blocks != null) {
			for(int i=0; i<blocks.size(); i++) {
				Task task = new Task(blocks.get(i), worker.nextNode());
				Global.exec.submit(task);
			}
		}
		long t2 = System.currentTimeMillis();
		long time_interval = t2 -t1;
		long work_time = afterWork - beforeWork;
		System.out.println("taskNum: "+this.dataBuffer.getCurNum()+", ThreadNum"+Thread.currentThread().getId()+", cost Time: "+time_interval+", workTime: "+work_time);
		if(shutDown) {
			Global.endTimer();
			System.out.println("MyThreadPool time consume: "+Global.getPassTime());
			Global.showStepOutputBuffer(worker.getStepOutputBuffer());
			Global.exec.shutdown();
		}
	}

}