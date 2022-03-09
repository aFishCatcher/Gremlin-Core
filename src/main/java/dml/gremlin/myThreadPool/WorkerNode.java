package dml.gremlin.myThreadPool;

public class WorkerNode {
	private final LinkedBuffer outBuffer;
	private final WorkerNode next_node;
	private Worker<LinkedBuffer, LinkedBuffer> worker;
	
	public WorkerNode(Worker _worker, LinkedBuffer _outBuffer, WorkerNode _next_node) {
		this.worker = _worker;
		this.outBuffer = _outBuffer;
		this.next_node = _next_node;
	}
	
	public LinkedBuffer outLinkedBuffer() {
		return outBuffer;
	}
	
	public WorkerNode nextNode() {
		return next_node;
	}
	
	public void work(LinkedBuffer in, LinkedBuffer out) {
		worker.work(in, out);
	}
}
