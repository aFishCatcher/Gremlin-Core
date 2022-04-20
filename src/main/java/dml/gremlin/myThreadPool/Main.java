package dml.gremlin.myThreadPool;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import dml.gremlin.myThreadPool.util.GraphTools;
import dml.gremlin.myThreadPool.util.OpenGremlin;
import org.apache.tinkerpop.gremlin.process.traversal.util.FastNoSuchElementException;

public class Main {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		//OpenGremlin.run();
		Main.run();

		
	}
	
	public static void run() throws InterruptedException, ExecutionException {
		final GraphTraversalSource g = GraphTools.loadGraph();  //load graph
		Traversal t = GraphTools.loadTraversal(g);  //load traversal query
		WorkerNode source = genWorkerNodes(t);  //construct WorkerNodes
		//WorkerNode source = genTestWorkerNodes();
				
		//construct source task, there is only one source task
		SourceTask task = new SourceTask(source);
		MyPool exec = MyPool.instance();
		Future<TaskList> f = exec.submit(task);
		TaskList taskList = f.get();
		long start = System.currentTimeMillis();
		//System.out.println("SourceTime: "+(System.currentTimeMillis()-start));
		while(!taskList.isEmpty()) {
			List<Future<TaskList>> fs = exec.invokeAll(taskList);
			taskList = new TaskList();
			for(Future<TaskList> future:fs) {
				taskList.addAll(future.get());
				//System.out.println("Time: "+(System.currentTimeMillis()-start));
			}
			//System.out.println("Time: "+(System.currentTimeMillis()-start));
		}
		long end = System.currentTimeMillis();
		exec.shutdown();
		System.out.println("time cost: "+(end-start));
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
		WorkerNode node = new WorkerNode(last_step, new StepOutputBuffer(), null, stepList.size()-1);  
		
		//generate rest nodes
		for(int i=stepList.size()-2; i>=0; i--) {
			node = new WorkerNode((Step)stepList.get(i), new StepOutputBuffer(), node, i);
		}
		
		return node;
	}
	
}
