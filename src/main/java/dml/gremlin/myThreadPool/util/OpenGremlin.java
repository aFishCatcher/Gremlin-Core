package dml.gremlin.myThreadPool.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

public class OpenGremlin {
	public static void run() {
		final GraphTraversalSource g = GraphTools.loadGraph();  //load graph
		Traversal t = GraphTools.loadTraversal(g);  //load traversal query
		
		long start = System.currentTimeMillis();
		List result = t.toList();
		long end = System.currentTimeMillis();
		System.out.println("OpenGremlin time consume: "+(end - start));
		
		show(result);
	}
	
	private static List testTime(Supplier<List> action) {
		long start = System.currentTimeMillis();
		List l = action.get();
		long end = System.currentTimeMillis();
		System.out.println("time consume: "+(end - start));
		
		return l;
	}
	
	private static void show(List result) {
		try {
			PrintWriter out;
			out = new PrintWriter("output/openGremlinResult.txt");
			Iterator it = result.iterator();
			while(it.hasNext())
				out.println(it.next());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
