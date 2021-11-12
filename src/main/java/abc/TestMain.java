package abc;

import java.util.HashSet;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import dml.stream.util.ArrayDequeBuffer;

public class TestMain {
	public static void main(String[] args) {
		TinkerGraph graph = TinkerGraph.open();
		GraphTraversalSource g = new GraphTraversalSource(graph);
		String dataPath = "testdata/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		
		GraphTraversal<Vertex, Vertex> gt = g.V().property("value0", 0);
		gt.preApplyStrategies();
		Step step0 = gt.asAdmin().getStep(0);
		Step step1 = gt.asAdmin().getStep(1);
		Step step2 = gt.asAdmin().getStep(2);
		ArrayDequeBuffer<Traverser> step0_step1 = new ArrayDequeBuffer<Traverser>();
		ArrayDequeBuffer<Traverser> step1_step2 = new ArrayDequeBuffer<Traverser>();
		step0.setProducer(step0_step1);
		step1.setConsumer(step0_step1);
		step1.setProducer(step1_step2);
		step2.setConsumer(step1_step2);
		step0.init();
		step1.init();
		step2.init();
		step0.work();
		step1.work();
		step2.work();
		
		String s = "123";
		System.out.println(s.substring(0, 1));
		System.out.println(s.substring(1));
	}
}
