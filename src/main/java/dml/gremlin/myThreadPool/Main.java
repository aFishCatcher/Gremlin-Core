package dml.gremlin.myThreadPool;

import java.util.Iterator;
import java.util.List;
import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import dml.gremlin.myThreadPool.util.GraphTools;
import dml.gremlin.myThreadPool.util.OpenGremlin;
import org.apache.tinkerpop.gremlin.process.traversal.util.FastNoSuchElementException;

public class Main {
	public static void main(String[] args) {
		//runOpenGremlinForCompare();
		//Main.run();

		Worker worker = new Worker<TaskDataBuffer, TaskDataBuffer>() {
			@Override
			public void work(TaskDataBuffer in, TaskDataBuffer out) {
				Iterator it = in.iterator();
				while(it.hasNext()){
					int ran = 500;
					for(int i=0; i< ran; i++)
						out.add(i);
					it.next();
					
				}
			}
		};
		WorkerNode last = new WorkerNode(worker, new StepOutputBuffer(), null);
		for(int i=0; i<0; i++){
			last = new WorkerNode(worker, new StepOutputBuffer(), last);
		}
		Worker source = new Worker<TaskDataBuffer, TaskDataBuffer>(){
			public void work(TaskDataBuffer in, TaskDataBuffer out) {
				for(int i=0; i<20000-1; i++)
					out.add(i);
				throw  FastNoSuchElementException.instance();
			}
		};
		WorkerNode first = new WorkerNode(source, new StepOutputBuffer(), last);
		SourceTask task = new SourceTask(first);
		task.run();
	}
	
	public static void run() {
		final GraphTraversalSource g = GraphTools.loadGraph();  //load graph
		Traversal t = GraphTools.loadTraversal(g);  //load traversal query
		WorkerNode source = genWorkerNodes(t);  //construct WorkerNodes
		
		//construct source task, there is only one source task
		SourceTask task = new SourceTask(source);
		task.run();
		//Global.exec.execute(task);
	}
	
	/*
	 * 生成WorkerNodes
	 * 返回第一个Node, SourceNode
	 */
	@SuppressWarnings("rawtypes")
	private static WorkerNode genWorkerNodes(Traversal t) {
		List stepList = t.asAdmin().getSteps();
		//generate last node
		Step last_step = (Step)stepList.get(stepList.size()-1);
		WorkerNode node = new WorkerNode(last_step, new StepOutputBuffer(), null);  
		
		//generate rest nodes
		for(int i=stepList.size()-2; i>=0; i--) {
			node = new WorkerNode((Step)stepList.get(i), new StepOutputBuffer(), node);
		}
		
		return node;
	}
	
	private static void runOpenGremlinForCompare() {
		OpenGremlin.run();
	}
}
