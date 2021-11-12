package abc;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.choose;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.constant;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.identity;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.math;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.values;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.AbstractStep;
import org.apache.tinkerpop.gremlin.process.traversal.util.TraversalInterruptedException;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.CloseableIterator;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class TestaExe {
	
	Graph graph = null;
	GraphTraversalSource g = null;
	
	public TestaExe() {}
	
	public void localPageRank1(int times) {
		System.out.println("localPageRank(times) Start...");
		graph = TinkerGraph.open();
		g = new GraphTraversalSource(graph);
		String dataPath = "data/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		g.V().property("pageRank", 1d).property("edgeCount", outE().count()).iterate();
		for(int i = 0; i < times; i++) {
			g.V().property("sendPoints", identity().as("a").as("b").choose(values("edgeCount").is(0),constant(0d),math("a/b").by("pageRank").by("edgeCount"))).iterate();
			g.V().property("pageRank", choose(__.in().count().is(0),constant(0.15d/6),identity().as("d").math("0.15/6+0.85*d").by(__.in().values("sendPoints").sum()))).iterate();
		}
		System.out.println(g.V().values("pageRank").toList());
		System.out.println("localPageRank(times) End...");
	}
	
	public void localPageRank2(int times) {
		System.out.println("localPageRank2(times) Start...");
		graph = TinkerGraph.open();
		g = new GraphTraversalSource(graph);
		String dataPath = "data/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		g.V().property("lastPageRank", 1d).property("edgeCount", outE().count()).iterate();
		g.V().property("lastSendPoints", identity().as("a").as("b").choose(
																		values("edgeCount").is(0),
																		constant(0d),
																		math("a/b").by("lastPageRank").by("edgeCount")
																		)
																	).iterate();
		for(int i = 0; i < times; i++) {
			g.V().property("pageRank", choose(
											__.in().count().is(0),
											constant(0.15d/6),
											identity().as("d").math("0.15/6+0.85*d").by(__.in().values("lastSendPoints").sum())
											)
										).iterate();
			g.V().property("sendPoints", identity().as("a").as("b").choose(
																		values("edgeCount").is(0),
																		constant(0d),
																		math("a/b").by("lastPageRank").by("edgeCount")
																		)
																	).iterate();
			g.V().property("lastPageRank", values("pageRank")).property("lastSendPoints", values("sendPoints")).iterate();
		}
		
		System.out.println(g.V().values("pageRank").toList());
		System.out.println("localPageRank2(times) End...");
	}
	
	public void localPageRank3(int times) {
		System.out.println("localPageRank3(times) Start...");
		graph = TinkerGraph.open();
		g = new GraphTraversalSource(graph);
		String dataPath = "data/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		
		g.V().property("赋值", 1).iterate();
		g.E().propertyMap().iterate();

		System.out.println("localPageRank3(times) End...");
	}
}
