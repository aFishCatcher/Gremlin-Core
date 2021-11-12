package dml.gremlin.general;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class TraversalThread extends Thread {
	
	private transient GraphTraversalSource g;
	private StepsList stepsList;
	
	public TraversalThread(GraphTraversalSource g, StepsList stepsList) {
		this.g = g;
		this.stepsList = stepsList;
	}
	
	public void run() {
		GraphTraversal gt = null;
		Method m;
		try {
			
			for(Step step : this.stepsList.steps) {
				switch(step.getOperator()) {
					case Step.StepName.g:
						break;
					case Step.StepName.E:
						gt = g.V(step.getArguments());
						break;
					case Step.StepName.V:
						gt = g.V(step.getArguments());
						break;
					case Step.StepName.print:
						System.out.println(gt.toList());
						break;
					default:
						m = GraphTraversal.class.getDeclaredMethod(step.getOperator(), step.getClasses());
						gt = (GraphTraversal) m.invoke(gt, (Object[]) step.getArguments());
				}
			}

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
