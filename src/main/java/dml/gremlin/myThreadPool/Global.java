package dml.gremlin.myThreadPool;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global {
	private static final int NTHREADS = 1;
	public static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static final int blockSize =5000;  //taskBuffer块的大小
	
	private static volatile long startTime = 0;
	private static volatile long endTime = 0;
	
	public static void startTimer() {
		startTime = System.currentTimeMillis();
	}
	
	public static void endTimer() {
		endTime = System.currentTimeMillis();
	}
	
	public static long getPassTime() {
		return endTime-startTime;
	}
	
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
