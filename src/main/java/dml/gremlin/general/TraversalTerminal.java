package dml.gremlin.general;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

// 暂未使用

public class TraversalTerminal {
	protected transient GraphTraversalSource g;
	protected StepsList stepsList = new StepsList();
	
	public TraversalTerminal(TraversalProgress tp) {
		this.g = tp.getGraphTraversalSource();
		this.stepsList = tp.getStepsList();
	}

	public GraphTraversalSource getGraphTraversalSource() {
		return this.g;
	}

	public StepsList getStepsList() {
		return this.stepsList;
	}

}
