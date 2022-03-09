package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Test {
	private static StringBuilder info = new StringBuilder();  //记录graph_name, traversal, 执行时间等信息
	private static String graph_name;
	private static int traversal_num;
	private static int thread_num;
	private static int max_rank;
	
	/*
	 * args[0]: graph_name. all graphs: ldbc-0.1  ,  grateful-dead  ,  tinkerpop-modern
	 * args[1]: traversal_num, indicating which traversal to test
	 * args[2]: thread_num, how many threads to execute
	 * args[3]: max_rank, max iteration
	 */
	public static void main(String[] args) throws IOException {
		
		if(args.length<4) {
			//default setting
			graph_name="grateful-dead";
			traversal_num = 0;
			thread_num = 2;
			max_rank = 1;
		}else {
			graph_name=args[0];
			traversal_num = Integer.valueOf(args[1]);
			thread_num = Integer.valueOf(args[2]);
			max_rank = Integer.valueOf(args[3]);
		}
		
		final GraphTraversalSource g = loadGraph(graph_name);  //加载图
		Traversal t = loadTraversal(g,traversal_num);  //加载traversal语句
		
		//设置全局变量
		Global.DATA_NUM = g.V().count().next().intValue();
		Global.N_STAGE = t.asAdmin().getSteps().size();
		
		for(int i=0; i<max_rank; i++) {
			info.append("rank"+i+"/\n");
			execOriginal(t.asAdmin().clone());  //原执行方式
			execSDF(t.asAdmin().clone());  //同步数据流执行
		}
		info.append("\n");
		
		
		String outFileName = "data/"+graph_name+"_info.txt";
		boolean add_at_end = !((traversal_num==0)&&(thread_num==1));
		try(FileWriter out = new FileWriter(outFileName, add_at_end)){
			out.write(info.toString());
		}
		//System.out.print(info);
	}
	
	private static GraphTraversalSource loadGraph(String graph_name) {
		String path = "data/"+graph_name+".xml";
		final GraphTraversalSource g = new GraphTraversalSource(TinkerGraph.open());
		g.io(path).read().iterate();

		return g;
	}
	
	private static Traversal loadTraversal(GraphTraversalSource g, int traversal_num) {
		Traversal t;
	    String traversal_string = "";
		switch(traversal_num) {
		case 0:
			t = g.V().identity();
			traversal_string = "g.V().identity()";
			break;
		case 1:
			t=g.V().identity().identity().identity().identity()
					.identity().identity().identity().identity()
					.identity().identity().identity().identity()
					.identity().identity().identity().identity();
			traversal_string = "g.V().identity().identity()... 16 identity() at all";
			break;
		case 2:
			t=g.V().hasLabel("person").out("knows").out("workAt");  //for data-0.1.xml
			traversal_string = "g.V().hasLabel(\"person\").out(\"konws\").out(\"workAt\")";
			break;
		case 3:
			t=g.V().both().both();
			traversal_string = "g.V().both().both()";
			break;
		case 4:
			t = g.V().hasLabel("person").out("knows").out("created");  //for tinkerpop-modern.xml
			traversal_string = "g.V().hasLabel(\"person\").out(\"konws\").out(\"created\")";
			break;
		default:
			t = g.V().identity();
			traversal_string = "g.V().identity()";
		}
		
		info.append("-----Traversal: "+traversal_string+"\n");
		info.append("-----Thread Num: "+thread_num+"\n");
		
		return t;
	}
	
	private static void execOriginal(Traversal t) {
		long start = 0, end = 0;
		start = System.currentTimeMillis();  //time start
		Iterator it = t.toList().iterator();
		end = System.currentTimeMillis();    // time end
		
		//记录原方案执行时间
		info.append("original total time: "+ (end-start)+ "ms\n");
				
		//记录原方案的执行结果
		//recordResult(it, "original_output.txt");
	}
	
	private static void execSDF(Traversal t) {
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
		
		//构建task、thread并启动线程
		final int Thread_Num = Math.min(thread_num, workerList.size());
		final Barrier barrier = new Barrier(Thread_Num);
		Task[] taskList = new Task[Thread_Num];
		for(int i=0; i<Thread_Num; i++) 
			taskList[i] = new Task(workerList.subList(i*workerList.size()/Thread_Num, (i+1)*workerList.size()/Thread_Num), barrier, i);
		FutureTask<String>[] futures = new FutureTask[Thread_Num];
		Thread[] threads = new Thread[Thread_Num];
		for(int i=0; i<Thread_Num; i++){
			futures[i] = new FutureTask<String>(taskList[i]);
			threads[i] = new Thread(futures[i]);
			threads[i].start();
		}
		
		//获取子thread运行时间信息
		for(FutureTask<String> future: futures) {
			try {
				info.append(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		//记录SDF结果
		//recordResult(bufferList.get(bufferList.size()-1).iterator(), "SDF_output.txt");
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

}
