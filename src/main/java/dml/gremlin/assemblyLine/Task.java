package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

public class Task implements Runnable{
	private static volatile int next_id = 0;
	
	private final int id;
	private final List<Worker> workers;
	private final Barrier barrier;
	
	public Task(List<Worker> _workers, Barrier _barrier) {
		this.workers = new ArrayList<>();
		workers.addAll(_workers);
		this.barrier = _barrier;
		id = next_id++;
	}

	@Override
	public void run() {
		PrintWriter out = null;
		try {
		  out = new PrintWriter(Thread.currentThread().getId()+"Log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		long cal=0, syn=0, temp_cal=0, temp_syn=0;
		long c1=0, c2=0, c3=0; 
		int stage[] = new int[Global.N_STAGE];
		stage[0] = 1;
		for(int stageNum=0; stageNum<Global.DATA_NUM+Global.N_STAGE-1;stageNum++) {
			my_await();
			//干活
			c1 = System.nanoTime();
			for(Worker worker: workers) {
				if(stage[worker.getStageNum()]==1) {
					worker.work();
				}
			}
			
			
			//管理stage[]
			for(int index=Global.N_STAGE-1; index>=1; index--)
				stage[index] = stage[index-1];
			if(stageNum==(Global.DATA_NUM-1))
				stage[0]=0;
			
			c2 = System.nanoTime();
			temp_cal = c2-c1;
			cal += c2-c1;
			
			//同步
			my_await();
			
			c3 = System.nanoTime();
			temp_syn = c3-c2;
			syn += c3-c2;
			out.printf("%10d,%10d, %d--%d--%d\n",temp_cal/1_000,temp_syn/1_000, c1, c2,c3 );
			//out.printf("cal: %10dns syn: %10dns\n",cal, syn);
			
		}
		out.close();
		System.out.println("thread id: "+ Thread.currentThread().getId()+" cal: "+ cal/1_000_000+"ms syn: "+syn/1_000_000+"ms");
		
		
	}
	
	
	private void my_await() {
		if(id==0) barrier.masterSync();
		else barrier.workerSync(id);
	}

}
