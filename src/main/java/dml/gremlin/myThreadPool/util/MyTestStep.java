package dml.gremlin.myThreadPool.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser.Admin;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.ScalarMapStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.AbstractStep;

import dml.gremlin.myThreadPool.TaskDataBuffer;

public class MyTestStep<S> extends ScalarMapStep<S, Double>{

	public MyTestStep(Traversal.Admin traversal) {
		super(traversal);
	}

	@Override
	protected Traverser.Admin<Double> processNextStart(){
		throw new UnsupportedOperationException("MyTestStep.processNextStart");
	}
	
	@Override
	public TaskDataBuffer<Admin<Double>> work(TaskDataBuffer<Traverser.Admin<S>> in){
		TaskDataBuffer<Traverser.Admin<Double>> out = new TaskDataBuffer<>(in.getCurNum(), in.isEnd());
		Iterator<Traverser.Admin<S>> it = in.iterator();
		while(it.hasNext()) {
			Traverser.Admin<S> t = it.next();
			out.add(t.split(this.map(t),this));
		}
		return out;
	}
	
	private Double myCompute() {
		int start = (int)(Math.random()*100);
		int end = (int)(5000)+start;
		int sum=0;
		for(int i=start; i<end; i++)
			sum+=i;
		return Double.valueOf(sum);
	}

	@Override
	protected Double map(Admin<S> traverser) {
		
		return myCompute();
	}

}
