package dml.gremlin.myThreadPool.util;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GraphTools {
	private final static String graph_dir = "data/";
	private final static String graph_name = "ldbc-0.1";
												// "tinkerpop-modern";
												// "grateful-dead";
												// "ldbc-0.1";
	private final static String graph_suffix = ".xml";
	
	public static GraphTraversalSource loadGraph() {
		String graph = graph_dir + graph_name + graph_suffix;
		final GraphTraversalSource g = new GraphTraversalSource(TinkerGraph.open());
		g.io(graph).read().iterate();
		return g;
	}
	
	public static Traversal loadTraversal(GraphTraversalSource g) {
		Traversal t =
				g.V().out();
		return t;
		
		/*common traversal query
		 * g.V().out().out();
		 * g.V().out().in();
		 * g.E().outV();
		 * g.V().math("1+2+3+4");
		 * 
		 */
		
		/*ldbc traversal query
		 * 
		 * g.V().hasLabel("person").values("firstName", "lastName");
		 * g.V().hasLabel("person").out("WorkAt");
		 */
		
	}
	
	private void information() {
		/*ldbc
		 * <key id="labelV" for="node" attr.name="labelV" attr.type="string"/>
			<key id="firstName" for="node" attr.name="firstName" attr.type="string"/>
			<key id="lastName" for="node" attr.name="lastName" attr.type="string"/>
			<key id="gender" for="node" attr.name="gender" attr.type="string"/>
			<key id="birthday" for="node" attr.name="birthday" attr.type="string"/>
			<key id="locationIP" for="node" attr.name="locationIP" attr.type="string"/>
			<key id="browserUsed" for="node" attr.name="browserUsed" attr.type="string"/>
			<key id="LocationCityId" for="node" attr.name="LocationCityId" attr.type="string"/>
			<key id="language" for="node" attr.name="language" attr.type="string"/>
			<key id="email" for="node" attr.name="email" attr.type="string"/>
			<key id="type" for="node" attr.name="type" attr.type="string"/>
			<key id="name" for="node" attr.name="name" attr.type="string"/>
			<key id="url" for="node" attr.name="url" attr.type="string"/>
			<key id="LocationPlaceId" for="node" attr.name="LocationPlaceId" attr.type="string"/>
			<key id="labelE" for="edge" attr.name="labelE" attr.type="string"/>
			<key id="classYear" for="edge" attr.name="classYear" attr.type="string"/>
			<key id="workFrom" for="edge" attr.name="workFrom" attr.type="string"/>
		 */
	}
}
