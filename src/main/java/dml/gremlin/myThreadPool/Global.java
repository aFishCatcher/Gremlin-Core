package dml.gremlin.myThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Global {
	private static final int NTHREADS = 10;
	public static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static final int blockSize = 100;  //taskBuffer块的大小
	
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
}
