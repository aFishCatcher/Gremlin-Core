package dml.gremlin.myThreadPool;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public class Task implements Runnable{
	private final int blockSize = Global.blockSize;
	private final TaskDataBuffer<Traverser> dataBuffer;
	private final WorkerNode worker;
	
	public Task(TaskDataBuffer<Traverser> dataBuffer, WorkerNode worker) {
		this.dataBuffer = dataBuffer;
		this.worker = worker;
	}
	
	@Override
	public void run() {
		TaskDataBuffer<Traverser> tempResult = new TaskDataBuffer<Traverser>(dataBuffer.getCurNum(), dataBuffer.isEnd());
		
		//处理数据, 结果放在临时缓冲区
		worker.work(dataBuffer, tempResult);

		//synchronized(this.worker.getStepOutputBuffer()) 
		{
			//将临时缓冲区合并到输出缓冲区，并尝试取出待处理数据块
			List<TaskDataBuffer> blocks = worker.mergeAndSplit(tempResult, this.blockSize);
	
			//construct new task
			if(blocks != null) {
				for(int i=0; i<blocks.size(); i++) {
					Task task = new Task(blocks.get(i), worker.nextNode());
					Global.exec.execute(task);
				}
			}
		}
		
		
		boolean shutDown = worker.isEndWorker() && dataBuffer.isEnd();
		if(shutDown) {
			Global.endTimer();
			System.out.println("MyThreadPool time consume: "+Global.getPassTime());
			Global.exec.shutdown();
		}
	}

}

class MutableBoolean{
	private boolean value = false;
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
}