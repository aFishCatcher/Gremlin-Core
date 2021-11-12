package abc;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.map.GraphStep;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.process.traversal.step.sideEffect.TinkerGraphStep;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import cde.*;
import dml.costream.lib.Barrier;
import dml.costream.lib.GetOpt;
import dml.stream.util.ArrayDequeBuffer;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.values;

public final class Testc {
	
	public static void antitest(GraphTraversalSource g) {
		long c0 = System.currentTimeMillis();
		g.V().property("value0", 0).property("value0", 10).property("value0", 20).property("value0", 30).
				property("value0", 40).property("value0", 50).property("value0", 60).property("value0", 70).
				property("value1", 80).property("value1", 90).property("value1", 100).property("value1", 110).
				property("value1", 35).property("value1", 55).property("value1", 85).property("value1", 160).
				property("value2", 40).property("value2", 25).property("value2", 150).property("value2", 95).
				property("value2", 60).property("value2", 75).property("value2", 195).property("value2", 145).
				property("value3", 215).property("value3", 230).property("value3", 105).property("value3", 275).
				property("value3", 55).property("value3", 75).property("value3", 25).property("value3", 95).
				property("value4", 85).property("value4", 115).property("value4", 335).property("value4", 975).
				iterate();
		long c1 = System.currentTimeMillis();
		System.out.println(c1 - c0);
	}
	
	//public static void func() {
	public static void main(String[] args) {
		System.out.println("Main Start...");
		TinkerGraph graph = TinkerGraph.open();
		GraphTraversalSource g = new GraphTraversalSource(graph);
		String dataPath = "testdata/grateful-dead.xml";
		g.io(dataPath).read().iterate();

		antitest(g);
		
		GraphTraversal<Vertex, Vertex> gt =
				g.V().property("value0", 0).property("value0", 10).property("value0", 20).property("value0", 30).
				property("value0", 40).property("value0", 50).property("value0", 60).property("value0", 70).
				property("value0", 80).property("value0", 90).property("value0", 100).property("value0", 110).
				property("value1", 35).property("value1", 55).property("value1", 85).property("value1", 160).
				property("value1", 40).property("value1", 25).property("value1", 150).property("value1", 95).
				property("value1", 60).property("value1", 75).property("value1", 195).property("value1", 145).
				property("value2", 215).property("value2", 230).property("value2", 105).property("value2", 275).
				property("value2", 55).property("value2", 75).property("value2", 25).property("value2", 95).
				property("value2", 85).property("value2", 115).property("value2", 335).property("value2", 975);
		List<Step> stepList = gt.asAdmin().getSteps();
		List<ArrayDequeBuffer<Traverser>> buffers = new ArrayList<>();
		for(int i=0; i<stepList.size(); i++)
			buffers.add(new ArrayDequeBuffer<>());
		for(int i=0; i<stepList.size(); i++)
		{
			Step step = stepList.get(i);
			if(i==0) step.setProducer(buffers.get(i));
			else {
				step.setConsumer(buffers.get(i-1));
				step.setProducer(buffers.get(i));
			}
		}
		
		
		Global.MAX_ITER = 800;
		int threadNum = 2;
		//Thread[] threads = new Thread[threadNum];
		Barrier barrier = new Barrier(threadNum);
		
		Thread_0 thread_0 = new Thread_0(barrier);
		Thread_1 thread_1 = new Thread_1(barrier);
//		Thread_2 thread_2 = new Thread_2(barrier);
//		Thread_3 thread_3 = new Thread_3(barrier);
		
		for(int i=0; i<(stepList.size()*1)/threadNum; i++)
			thread_0.addStep(stepList.get(i));
		
		for(int i=(stepList.size()*1)/threadNum; i<(stepList.size()*2)/threadNum; i++)
			thread_1.addStep(stepList.get(i));
//		
//		for(int i=(stepList.size()*2)/threadNum; i<(stepList.size()*3)/threadNum; i++)
//			thread_1.addStep(stepList.get(i));
//		
//		for(int i=(stepList.size()*3)/threadNum; i<(stepList.size()*4)/threadNum; i++)
//			thread_1.addStep(stepList.get(i));
		
		thread_0.start();
		thread_1.start();
//		thread_2.start();
//		thread_3.start();
		System.out.println("Main End...");
	}
}
