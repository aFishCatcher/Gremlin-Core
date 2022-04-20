package dml.gremlin.myThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyPool extends ThreadPoolExecutor {
	private long start = System.currentTimeMillis();
	private static final int NTHREADS =2;
	private static final MyPool INSTANCE = new MyPool(NTHREADS, NTHREADS, 0L, TimeUnit.MILLISECONDS,
													new LinkedBlockingQueue<Runnable>());
	public MyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public static MyPool instance() {
		return INSTANCE;
	}
	
//	@Override
//    protected void afterExecute(Runnable r, Throwable t) {
//		super.afterExecute(r, t);
//	     if (t == null
//	         && r instanceof Future<?>
//	         && ((Future<?>)r).isDone()) {
//	       try {
//	         TaskList result =(TaskList)((Future<?>) r).get();
//	         if(!result.isEmpty()) {
//	        	 for(int i=0; i<result.size(); i++)
//	        		 this.submit((Task)result.get(i));
//	         }
//	         if(result.isEnd()) {
//	        	 long end = System.currentTimeMillis();
//	        	 System.out.println("time cost: "+(end-start));
//	        	 this.shutdown();
//	         }
//	       } catch (CancellationException ce) {
//	         t = ce;
//	       } catch (ExecutionException ee) {
//	         t = ee.getCause();
//	       } catch (InterruptedException ie) {
//	         // ignore/reset
//	         Thread.currentThread().interrupt();
//	       }
//	     }
//	     if (t != null)
//	       System.out.println(t);
//	}

}
