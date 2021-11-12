package dml.gremlin.general;

import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class TraversalSource {
	protected transient GraphTraversalSource g;
	protected StepsList stepsList = new StepsList ();
	
	public TraversalSource(TraversalStart ts) {
		this.g = ts.getGraphTraversalSource();
		this.stepsList = ts.getStepsList();
	}
	
	public TraversalProgress V() {
		this.stepsList.steps.add(new Step(Step.StepName.V, new Class[] {}, new Object[] {}));
		return new TraversalProgress(this);
	}
	
	public TraversalProgress V(final Object... vertexIds) {
		Class[] classes = {Object[].class};
		Object[] arguments = {vertexIds};
		this.stepsList.steps.add(new Step(Step.StepName.V, classes, arguments));
		return new TraversalProgress(this);
	}
	
	public TraversalProgress E() {
		this.stepsList.steps.add(new Step(Step.StepName.E, new Class[] {}, new Object[] {}));
		return new TraversalProgress(this);
	}
	
	public TraversalProgress E(final Object... edgesIds) {
		Class[] classes = {Object[].class};
		Object[] arguments = {edgesIds};
		this.stepsList.steps.add(new Step(Step.StepName.E, classes, arguments));
		return new TraversalProgress(this);
	}
	
	public GraphTraversalSource getGraphTraversalSource() {
		return this.g;
	}

	public StepsList getStepsList() {
		return this.stepsList;
	}
}
