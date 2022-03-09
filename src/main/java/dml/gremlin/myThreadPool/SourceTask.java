package dml.gremlin.myThreadPool;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public class SourceTask extends Task {

	public SourceTask(WorkerNode _worker) {
		super(null, _worker);
	}
	
	@Override
	public void run() {
		int count = 5;
		while(count!=0) {
			LinkedBuffer<Traverser> temp_buffer = new LinkedBuffer<Traverser>();
			
			//生成数据
			worker.work(null, temp_buffer);

			//合并数据
			worker.outLinkedBuffer().mergeWith(temp_buffer);
			
			//检查是否需要构建新的task
			temp_buffer = worker.outLinkedBuffer().checkAndSplit();
			if(temp_buffer!=null && worker.nextNode()!=null) {
				Task task = new Task(temp_buffer, worker.nextNode());
				Main.exec.execute(task);
			}
			
			count--;
		}
	}

}
