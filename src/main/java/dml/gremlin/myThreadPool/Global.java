package dml.gremlin.myThreadPool;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

public class Global {
	public static final int blockSize =2500;  //taskBuffer块的大小

	@SuppressWarnings({ "rawtypes" })
	public static void showStepOutputBuffer(StepOutputBuffer stepOutputBuffer){
		String fileName = "output/myThreadPoolResult.txt";
		try(PrintWriter out = new PrintWriter(fileName)) {
			Iterator it = stepOutputBuffer.iterator();
			while(it.hasNext())
				out.println(it.next());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
