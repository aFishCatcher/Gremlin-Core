package dml.gremlin.usecase;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.IO;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.apache.tinkerpop.gremlin.process.traversal.Operator.*;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.*;
import static org.apache.tinkerpop.gremlin.process.traversal.P.*;
import static org.apache.tinkerpop.gremlin.process.traversal.Pop.*;
import static org.apache.tinkerpop.gremlin.process.traversal.SackFunctions.*;
import static org.apache.tinkerpop.gremlin.process.traversal.Scope.*;
import static org.apache.tinkerpop.gremlin.process.traversal.TextP.*;
import static org.apache.tinkerpop.gremlin.structure.Column.*;
import static org.apache.tinkerpop.gremlin.structure.Direction.*;
import static org.apache.tinkerpop.gremlin.structure.T.*;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.values;

public class UmPageRank {

	private transient GraphTraversalSource g;
	
	public UmPageRank(GraphTraversalSource g) {
		this.g = g;
		init();
	}
	
	private void init() {
		
	}
	
	public void run(int times) {
//		g.V().property("pageRank", 1d).property("edgeCount", outE().count()).repeat(
//			property("sendPoints",identity().as("a").as("b").choose(values("edgeCount").is(0),constant(0d),math("a/b").by("pageRank").by("edgeCount"))).
//			property("pageRank", choose(__.in().count().is(0),constant(0.15d/6),identity().as("d").math("0.15/6+0.85*d").by(__.in().values("sendPoints").sum())))
//			).times(20).elementMap("pageRank");
		g.V().property("pageRank", 1d).property("edgeCount", outE().count()).iterate();
		for(int i = 0; i < times; i++) {
			g.V().property("sendPoints",identity().as("a").as("b").choose(values("edgeCount").is(0),constant(0d),math("a/b").by("pageRank").by("edgeCount"))).iterate();
			g.V().property("pageRank", choose(__.in().count().is(0),constant(0.15d/6),identity().as("d").math("0.15/6+0.85*d").by(__.in().values("sendPoints").sum()))).iterate();
		}
		
		Number mysum = g.V().values("pageRank").sum().next();
		g.withSideEffect("x", mysum).V().property("pageRank", values("pageRank").math("_/x")).elementMap("pageRank");
	}
}
