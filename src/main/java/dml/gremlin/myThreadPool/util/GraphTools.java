package dml.gremlin.myThreadPool.util;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GraphTools {
	private final static String graph_dir = "data/";
	private final static String graph_name = "grateful-dead";
	private final static String graph_suffix = ".xml";
	
	public static GraphTraversalSource loadGraph() {
		String graph = graph_dir + graph_name + graph_suffix;
		final GraphTraversalSource g = new GraphTraversalSource(TinkerGraph.open());
		g.io(graph).read().iterate();
		return g;
	}
	
	public static Traversal loadTraversal(GraphTraversalSource g) {
		Traversal t = g.V().out();
		return t;
		
		/*some common traversal query
		 * g.V().out().out();
		 * 
		 */
		
	}
}
