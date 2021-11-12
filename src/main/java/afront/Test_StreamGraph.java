package afront;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Test_StreamGraph {
	public static void main(String[] args) {
		TinkerGraph graph = TinkerGraph.open();
		GraphTraversalSource g = new GraphTraversalSource(graph);
		String dataPath = "testdata/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		
		Traversal t;
		t = g.V().and(__.hasLabel("person"), __.out()).values("name");  //and
//		t = g.V().or(__.hasLabel("person"), __.out()).values("name");  //or
//		t = g.V().not(__.hasLabel("person")).values("name");  //not
//		t = g.V(2).optional(__.in("knows"));  //optional
//		t = g.V().out("knows").where(__.out("created")).values("name");  //where
//		t = g.V().choose(__.hasLabel("person"), __.out("created"), __.identity()).values("name");  //choose
//		t = g.V(4).union(__.in().values("age"), __.out().values("lang"));  //union
//		t = g.V().group().by(__.values("name"));  //group
//		t = g.V(1).repeat(__.out("knows")).until(__.hasLabel("person"));  //repeat
		
		StreamGraph sg = new StreamGraph();
		sg.process(t);
		sg.show();
	}
}
