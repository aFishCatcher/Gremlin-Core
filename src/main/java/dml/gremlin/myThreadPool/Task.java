package dml.gremlin.myThreadPool;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public class Task implements Runnable{
	private final LinkedBuffer<Traverser> dataBuffer;
	
	protected final WorkerNode worker;
	public Task(LinkedBuffer<Traverser> _dataBuffer, WorkerNode _worker) {
		dataBuffer = _dataBuffer;
		worker = _worker;
	}
	
	@Override
	public void run() {
		LinkedBuffer<Traverser> temp_buffer = new LinkedBuffer<Traverser>();
		
		//处理数据
		worker.work(dataBuffer, temp_buffer);

		//合并数据
		worker.outLinkedBuffer().mergeWith(temp_buffer);
		
		//检查是否需要构建新的task
		temp_buffer = worker.outLinkedBuffer().checkAndSplit();
		if(temp_buffer!=null && worker.nextNode()!=null) {
			Task task = new Task(temp_buffer, worker.nextNode());
			Main.exec.execute(task);
		}
	}

}
