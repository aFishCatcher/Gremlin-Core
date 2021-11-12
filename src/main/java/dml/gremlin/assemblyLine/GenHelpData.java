package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class GenHelpData {
	public static void main(String[] args) throws FileNotFoundException {
		TinkerGraph graph = TinkerGraph.open();
		GraphTraversalSource g = new GraphTraversalSource(graph);
		String dataPath = "testdata/grateful-dead.xml";
		g.io(dataPath).read().iterate();
		
		long t1 = System.currentTimeMillis();
		g.V().identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity()
		.identity().identity().identity().identity().toList().iterator();
		long t2 = System.currentTimeMillis();
		System.out.println("main time: "+(t2-t1));
		
		Runnable task = new Runnable(){
			public void run()
			{
				long c1 = System.currentTimeMillis();
				g.V().identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity()
				.identity().identity().identity().identity().toList().iterator();
				long c2 = System.currentTimeMillis();
				System.out.println(Thread.currentThread().getId()+" time: "+(c2-c1));
			}
		};
		
		new Thread(task).start();;
		new Thread(task).start();;
		
//		int N = 20;
//		for(int i=1; i<=N; i++) {
//			PrintWriter out = new PrintWriter("helpData/Node_"+i+".txt");
//			Iterator it = g.V(i+"").both().toList().iterator();
//			while(it.hasNext())
//				out.println(it.next());
//			out.close();
//		}
	}
	
	
}
