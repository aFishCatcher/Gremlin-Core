package dml.gremlin.general;

import org.apache.tinkerpop.gremlin.process.traversal.Operator;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class TraversalProgress {
	protected transient GraphTraversalSource g;
	protected StepsList stepsList = new StepsList();
	
	public TraversalProgress(TraversalSource ts) {
		this.g = ts.getGraphTraversalSource();
		this.stepsList = ts.getStepsList();
	}
	
	public GraphTraversalSource getGraphTraversalSource() {
		return this.g;
	}
	
	public StepsList getStepsList() {
		return this.stepsList;
	}
	
	public TraversalProgress as(final String stepLabel, final String... stepLabels) {
		Class[] classes = {String.class, String[].class};
		Object[] arguments = {stepLabel, stepLabels};
		this.stepsList.steps.add(new Step(Step.StepName.as, classes, arguments));
		return this;
	}
	
	public TraversalProgress barrier() {
		Class[] classes = {};
		Object[] arguments = {};
		this.stepsList.steps.add(new Step(Step.StepName.barrier, classes, arguments));
		return this;
	}
	
	public TraversalProgress barrier(final int maxBarrierSize) {
		Class[] classes = {int.class};
		Object[] arguments = {maxBarrierSize};
		this.stepsList.steps.add(new Step(Step.StepName.barrier, classes, arguments));
		return this;
	}
	
	public TraversalProgress both(final String... edgeLabels) {
		Class[] classes = {String[].class};
		Object[] arguments = {edgeLabels};
		this.stepsList.steps.add(new Step(Step.StepName.both, classes, arguments));
		return this;
	}
	
	public TraversalProgress by(final String key) {
		Class[] classes = {String.class};
		Object[] arguments = {key};
		this.stepsList.steps.add(new Step(Step.StepName.by, classes, arguments));
		return this;
	}
	
	public TraversalProgress cap(final String sideEffectKey, final String... sideEffectKeys) {
		Class[] classes = {String.class, String[].class};
		Object[] arguments = {sideEffectKey, sideEffectKeys};
		this.stepsList.steps.add(new Step(Step.StepName.cap, classes, arguments));
		return this;
	}
	
	public TraversalProgress coin(final double probability) {
		Class[] classes = {double.class};
		Object[] arguments = {probability};
		this.stepsList.steps.add(new Step(Step.StepName.coin, classes, arguments));
		return this;
	}
	
	public TraversalProgress count() {
		Class[] classes = {};
		Object[] arguments = {};
		this.stepsList.steps.add(new Step(Step.StepName.count, classes, arguments));
		return this;
	}
	
	public TraversalProgress cyclicPath() {
		Class[] classes = {};
		Object[] arguments = {};
		this.stepsList.steps.add(new Step(Step.StepName.cyclicPath, classes, arguments));
		return this;
	}
	
	public TraversalProgress dedup(final String... dedupLabels) {
		Class[] classes = {String[].class};
		Object[] arguments = {dedupLabels};
		this.stepsList.steps.add(new Step(Step.StepName.dedup, classes, arguments));
		return this;
	}
	
	public TraversalProgress elementMap(final String... propertyKeys) {
		Class[] classes = {String[].class};
		Object[] arguments = {propertyKeys};
		this.stepsList.steps.add(new Step(Step.StepName.elementMap, classes, arguments));
		return this;
	}
	
	public TraversalProgress from(final String fromStepLabel) {
		Class[] classes = {String.class};
		Object[] arguments = {fromStepLabel};
		this.stepsList.steps.add(new Step(Step.StepName.from, classes, arguments));
		return this;
	}
	
	public TraversalProgress group(final String sideEffectKey) {
		Class[] classes = {String.class};
		Object[] arguments = {sideEffectKey};
		this.stepsList.steps.add(new Step(Step.StepName.group, classes, arguments));
		return this;
	}
	
	public TraversalProgress groupCount(final String sideEffectKey) {
		Class[] classes = {String.class};
		Object[] arguments = {sideEffectKey};
		this.stepsList.steps.add(new Step(Step.StepName.groupCount, classes, arguments));
		return this;
	}
	
	public TraversalProgress has(final String propertyKey, final Object value) {
		Class[] classes = {String.class, Object.class};
		Object[] arguments = {propertyKey, value};
		this.stepsList.steps.add(new Step(Step.StepName.has, classes, arguments));
		return this;
	}
	
	public TraversalProgress hasLabel(final String label, final String... otherLabels) {
		Class[] classes = {String.class, String[].class};
		Object[] arguments = {label, otherLabels};
		this.stepsList.steps.add(new Step(Step.StepName.hasLabel, classes, arguments));
		return this;
	}
	
	public TraversalProgress in(final String... edgeLabels) {
		Class[] classes = {String[].class};
		Object[] arguments = {edgeLabels};
		this.stepsList.steps.add(new Step(Step.StepName.in, classes, arguments));
		return this;
	}
	
	public TraversalProgress out(final String... edgeLabels) {
		Class[] classes = {String[].class};
		Object[] arguments = {edgeLabels};
		this.stepsList.steps.add(new Step(Step.StepName.out, classes, arguments));
		return this;
	}
	
	public TraversalProgress property(final Object key, final Object value, final Object... keyValues) {
		Class[] classes = {Object.class, Object.class, Object[].class};
		Object[] arguments = {key, value, keyValues};
		this.stepsList.steps.add(new Step(Step.StepName.property, classes, arguments));
		return this;
	}
	
	public TraversalProgress select(final String selectKey1, final String selectKey2, String... otherSelectKeys) {
		Class[] classes = {String.class, String.class, String[].class};
		Object[] arguments = {selectKey1, selectKey2, otherSelectKeys};
		this.stepsList.steps.add(new Step(Step.StepName.select, classes, arguments));
		return this;
	}
	
	public TraversalProgress to(final String toStepLabel) {
		Class[] classes = {String.class};
		Object[] arguments = {toStepLabel};
		this.stepsList.steps.add(new Step(Step.StepName.to, classes, arguments));
		return this;
	}
	
	public TraversalProgress values(final String... propertyKeys) {
		Class[] classes = {String[].class};
		Object[] arguments = {propertyKeys};
		this.stepsList.steps.add(new Step(Step.StepName.values, classes, arguments));
		return this;
	}
	
	// 自行定义
	public TraversalStart print() {
		Class[] classes = {};
		Object[] arguments = {};
		this.stepsList.steps.add(new Step(Step.StepName.print, classes, arguments));
		return new TraversalStart(this);
	}

}
