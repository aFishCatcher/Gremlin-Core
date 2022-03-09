package dml.gremlin.myThreadPool;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Main {
	private final static String graph_dir = "data/";
	private final static String graph_name = "tinkerpop-modern";
	private final static String graph_suffix = ".xml";
	
	private static final int NTHREADS = 3;
	public static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static void main(String[] args) {
//		final GraphTraversalSource g = loadGraph();  //加载图
//		Traversal t = g.V().out();
//		WorkerNode source = genWorkerNodes(t);
//		
//		Task task = new SourceTask(source);  //construct source task
//		exec.execute(task);
		
		Runnable task = new Runnable() {
			public void run() {
				System.out.println("run..");
			}
		};
		exec.execute(task);
	}

	private static GraphTraversalSource loadGraph() {
		String graph = graph_dir + graph_name + graph_suffix;
		final GraphTraversalSource g = new GraphTraversalSource(TinkerGraph.open());
		g.io(graph).read().iterate();
		return g;
	}
	
	/*
	 * 生成WorkerNodes
	 * 返回第一个Node, SourceNode
	 */
	private static WorkerNode genWorkerNodes(Traversal t) {
		List stepList = t.asAdmin().getSteps();
		Step last_step = (Step)stepList.get(stepList.size()-1);
		WorkerNode node = new WorkerNode(last_step, new LinkedBuffer<Traverser>(), null);  //last node
		for(int i=stepList.size()-2; i>=0; i--) {
			node = new WorkerNode((Step)stepList.get(i), new LinkedBuffer<Traverser>(), node);
		}
		return node;
	}
}
