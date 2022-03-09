package dml.gremlin.assemblyLine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

public class Task implements Callable<String>{
	
	private final int id;
	private final List<Worker> workers;
	private final Barrier barrier;
	
	public Task(List<Worker> _workers, Barrier _barrier, int _id) {
		this.workers = new ArrayList<>();
		workers.addAll(_workers);
		this.barrier = _barrier;
		this.id = _id;
	}

	@Override
	public String call() {
		//用于输出log.txt
//		PrintWriter out = getPrintWriter();
		
		/*计时变量*/
		long cal=0, syn=0;  //total calculate time and synchronize time
		long temp_cal=0, temp_syn=0;  //calculate and synchronize time every iteration
		long c1=0, c2=0, c3=0;  //time cut point
		
		boolean stage[] = new boolean[Global.N_STAGE];
		stage[0] = true;
		for(int stageNum=0; stageNum<Global.DATA_NUM+Global.N_STAGE-1;stageNum++) {
			//barrier.my_await(id);
			//干活
			c1 = System.nanoTime();  //-----time cut point 1
			for(Worker worker: workers) {
				if(stage[worker.getStageNum()]) {
					worker.work();
				}
			}
			//管理stage[]
			for(int index=Global.N_STAGE-1; index>=1; index--)
				stage[index] = stage[index-1];
			if(stageNum==(Global.DATA_NUM-1))
				stage[0]=false;
			
			c2 = System.nanoTime();  //-----time cut point 2
			temp_cal = c2-c1;
			cal += c2-c1;
			
			//同步
			barrier.my_await(id);
			c3 = System.nanoTime();  //-----time cut point 3
			temp_syn = c3-c2;
			syn += c3-c2;
			//这里文件的读写似乎会影响到计算和同步的时间
			//out.printf("%10d,%10d, %d--%d--%d\n",temp_cal/1_000,temp_syn/1_000, c1, c2,c3 );
		}
//		out.close();
		
		String info = "task id: "+ id +" cal_time: "+ cal/1_000_000+"ms syn_time: "+syn/1_000_000+"ms\n";
		return info;
	}
	
	private PrintWriter getPrintWriter()
	{
		PrintWriter out = null;
		try {
			String name = "taskID_"+id+"_log.txt";
			out= new PrintWriter(name);
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		finally {
			return out;
		}
	}
}
