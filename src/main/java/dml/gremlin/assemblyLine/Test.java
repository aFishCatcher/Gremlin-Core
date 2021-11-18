package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Test {

	private static GraphTraversalSource loadGraph(String graph_name) {
		URL url = Test.class.getClassLoader().getResource(graph_name);
		String path = url.getPath();
		path = "D:/data/"+graph_name;
		final GraphTraversalSource g = new GraphTraversalSource(TinkerGraph.open());
		g.io(path).read().iterate();
		return g;
	}
	
	private static Traversal loadTraversal(GraphTraversalSource g) {
		Traversal t;
		//t = g.V().identity();
		
		t=g.V().identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity();
		
//		t=g.V().property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
//				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
//				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
//				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
//				.property("value0", 0).property("value0", 0).property("value0", 0).property("value0", 0)
//				;
		
		//t=g.V().both().both();
		
		//t = g.V().hasLabel("person").out("knows").out("created");  //for tinkerpop-modern.xml
		//t=g.V().hasLabel("person").out("knows").out("workAt");  //for data-0.1.xml
		
		return t;
	}
	
	private static void recordResult(Iterator it, String filename) {
		try {
			PrintWriter out = new PrintWriter(filename);
			while(it.hasNext())
				out.println(it.next());
			out.close();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String dataPath = "grateful-dead.xml";  //all paths: data-0.1.xml  ;  grateful-dead.xml  ;  tinkerpop-modern.xml
		final GraphTraversalSource g = loadGraph(dataPath);  //加载图
		Traversal t = loadTraversal(g);  //加载traversal语句 there are many traversal statements
		
		//原方案执行时间
		long start = 0, end = 0;
		Traversal t_clone = t.asAdmin().clone();
		start = System.currentTimeMillis();  //time start
		Iterator it = t_clone.toList().iterator();
		end = System.currentTimeMillis();    // time end
		System.out.println("Standard total time: "+ (end-start)+ "ms");
		
		//记录原方案的执行结果
//		recordResult(it, "standardOutput.txt");
		
		//构建buffer、worker
		List<Step> stepList = t.asAdmin().getSteps();
		List<Buffer> bufferList = new ArrayList<>();
		for(int i=0; i<stepList.size(); i++)
			bufferList.add(new Buffer<Traverser>());
		List<Worker> workerList = new ArrayList<>();
		for(int i=0; i<stepList.size(); i++) {
			if(i==0) workerList.add( new Worker(stepList.get(i), null, bufferList.get(i),true,i) );
			else workerList.add(new Worker(stepList.get(i), bufferList.get(i-1), bufferList.get(i),i));
		}
		
		//设置全局变量
		Global.DATA_NUM = g.V().count().next().intValue();
		Global.N_STAGE = stepList.size();
		
		//构建task、thread并启动线程
		final int Thread_Num = 2;
		final Barrier barrier = new Barrier(Thread_Num);
		Task[] taskList = new Task[Thread_Num];
		for(int i=0; i<Thread_Num; i++) 
			taskList[i] = new Task(workerList.subList(i*workerList.size()/Thread_Num, (i+1)*workerList.size()/Thread_Num), barrier);
		Thread[] threads = new Thread[Thread_Num];
		for(int i=0; i<Thread_Num; i++){
			threads[i] = new Thread(taskList[i]);
			threads[i].start();
		}
		
		//等所有子线程执行完后记录结果
//		try {
//			for(Thread thread: threads)
//				thread.join();
//			it = bufferList.get(bufferList.size()-1).iterator();
//			recordResult(it, "testOutput.txt");
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

}
