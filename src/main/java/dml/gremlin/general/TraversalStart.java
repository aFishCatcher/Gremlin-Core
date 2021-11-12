package dml.gremlin.general;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class TraversalStart {
	protected transient GraphTraversalSource g;
	protected StepsList stepsList = new StepsList ();
	
	public TraversalStart(GraphTraversalSource g) {
		this.g = g;
	}
	
	public TraversalStart(GraphTraversalSource g, StepsList stepsList) {
		this.g = g;
		this.stepsList = stepsList;
	}
	
	public TraversalStart(TraversalProgress tg) {
		this.g = tg.getGraphTraversalSource();
		this.stepsList = tg.getStepsList();
	}

	public TraversalSource g() {
		this.stepsList.steps.add(new Step(Step.StepName.g, new Class[] {}, new Object[] {}));
		return new TraversalSource(this);
	}
	
	public GraphTraversalSource getGraphTraversalSource() {
		return this.g;
	}
	
	public StepsList getStepsList() {
		return this.stepsList;
	}
	
	public void run() {
		ArrayList<StepsList> stepsLists = new ArrayList<StepsList> ();
		StepsList sl = new StepsList();
		for(Step s : this.stepsList.steps) {
			if(s.getOperator() == Step.StepName.g) {
				stepsLists.add(sl);
				sl = new StepsList();
			}
			sl.steps.add(s);
		}
		stepsLists.add(sl);
		ArrayList<TraversalThread> Threads = new ArrayList<TraversalThread> ();
		for(int i = 1, len = stepsLists.size(); i < len; i++) {
			Threads.add(new TraversalThread(g, stepsLists.get(i)));
		}
		for(TraversalThread t : Threads) {
			t.start();
		}
	}

}
