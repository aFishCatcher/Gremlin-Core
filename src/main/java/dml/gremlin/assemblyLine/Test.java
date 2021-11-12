package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Test {

	public static void main(String[] args) {
		//创建、加载图
		final TinkerGraph graph = TinkerGraph.open();
		final GraphTraversalSource g = new GraphTraversalSource(graph);
		String dataPath = "D:/Dir/data-0.1.xml";
		//dataPath = "testdata/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		
		Traversal t = g.V().hasLabel("person").out("knows").out("created");
		//t=g.V().both().both();
		t=g.V().identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity();
		t = g.V().identity();
		//t=g.V().hasLabel("person").out("knows").out("workAt");
		t=g.V().property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
				;
		try {
			PrintWriter out = new PrintWriter("STAND_output.txt");
			Traversal copy_t = t.asAdmin().clone();
			long start = System.currentTimeMillis();
			Iterator it = copy_t.toList().iterator();
			long end = System.currentTimeMillis();
			System.out.println("Stand total time: "+ (end-start)+ "ms");
			
			while(it.hasNext())
				out.println(it.next());
			out.close();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		List<Step> stepList = t.asAdmin().getSteps();
		List<Buffer> bufferList = new ArrayList<>();
		for(int i=0; i<stepList.size(); i++)
			bufferList.add(new Buffer<Traverser>());
		List<Worker> workerList = new ArrayList<>();
		for(int i=0; i<stepList.size(); i++) {
			if(i==0) workerList.add( new Worker(stepList.get(i), null, bufferList.get(i),true,i) );
			else workerList.add(new Worker(stepList.get(i), bufferList.get(i-1), bufferList.get(i),i));
		}
		
		Global.DATA_NUM = g.V().count().next().intValue()/1;
		Global.N_STAGE = stepList.size();
		
		//System.out.println("data_num: "+Global.DATA_NUM+" n_stage: "+Global.N_STAGE);
		final int Thread_Num = 2;
		final Barrier barrier = new Barrier(Thread_Num);
		Task[] taskList = new Task[Thread_Num];
//		taskList[0] = new Task(workerList.subList(0, 1),barrier);
//		taskList[1] = new Task(workerList.subList(1, workerList.size()/2), barrier);
//		taskList[2] = new Task(workerList.subList(workerList.size()/2, workerList.size()), barrier);
		for(int i=0; i<Thread_Num; i++) {
			taskList[i] = new Task(workerList.subList(i*workerList.size()/Thread_Num, (i+1)*workerList.size()/Thread_Num), barrier);
		}
		
		Thread[] threads = new Thread[Thread_Num];
		try {
			for(int i=0; i<Thread_Num; i++) 
				threads[i] = new Thread(taskList[i]);
			//Long start = System.currentTimeMillis();  //------time start
			for(int i=0; i<Thread_Num; i++) 
				threads[i].start();
			for(Thread thread: threads)
				thread.join();
//			Long end = System.currentTimeMillis();  //------time end
			//System.out.println("Test total time: "+ (end-start)+ "ms");
			
			int count = 0;
			PrintWriter out = new PrintWriter("Test_output.txt");
			while(!bufferList.get(bufferList.size()-1).isEmpty()) {
				Traverser traverser = (Traverser)bufferList.get(bufferList.size()-1).takeData();
				out.println(traverser.get());
				count++;
			}
			out.println("count: "+count);
			out.close();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		finally {
			
		}
		
		try {
			Analysis.analysis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
