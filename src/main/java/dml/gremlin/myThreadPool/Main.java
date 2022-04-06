package dml.gremlin.myThreadPool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import dml.gremlin.myThreadPool.util.GraphTools;
import dml.gremlin.myThreadPool.util.OpenGremlin;

public class Main {
	public static void main(String[] args) {
		runOpenGremlinForCompare();
		
		final GraphTraversalSource g = GraphTools.loadGraph();  //load graph
		Traversal t = GraphTools.loadTraversal(g);  //load traversal query
		WorkerNode source = genWorkerNodes(t);  //construct WorkerNodes
		
		//construct source task, there is only one source task
		Global.startTimer();
		SourceTask task = new SourceTask(source);  
		Global.exec.execute(task);
	}
	
	/*
	 * 生成WorkerNodes
	 * 返回第一个Node, SourceNode
	 */
	private static WorkerNode genWorkerNodes(Traversal t) {
		List stepList = t.asAdmin().getSteps();
		//generate last node
		Step last_step = (Step)stepList.get(stepList.size()-1);
		WorkerNode node = new WorkerNode(last_step, new StepOutputBuffer<Traverser>(), null);  
		
		//generate rest nodes
		for(int i=stepList.size()-2; i>=0; i--) {
			node = new WorkerNode((Step)stepList.get(i), new StepOutputBuffer<Traverser>(), node);
		}
		
		return node;
	}
	
	private static void runOpenGremlinForCompare() {
		OpenGremlin.run();
	}
}
