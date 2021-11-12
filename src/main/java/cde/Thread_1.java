/*该文件定义各thread的入口函数，在函数内部完成软件流水迭�?*/
package cde;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Step;

import dml.costream.lib.*;

public class Thread_1 extends Thread {

	private Barrier barrier;
	
	private List<Step> steps = new ArrayList<Step> ();

	public Thread_1(Barrier barrier) {
		this.barrier = barrier;
	}
	
	public void addStep(Step step) {
		this.steps.add(step);
	}

	public void run() {
		barrier.workerSync(1);
		long c0, c1;
		long cal = 0, total = 0;
		FileOutputStream fos;
		int stage[] = new int [4];
		stage[0]=1;
		long t0 = System.currentTimeMillis();
//		for(int stageNum=0; stageNum<4; stageNum++) {
//			c0 = System.currentTimeMillis();
//			if(2 == stageNum) {
//				for(Step step : steps) {
//					step.init();
//				}
//			}
//			c1 = System.currentTimeMillis();
//			cal += c1 - c0;
//			barrier.workerSync(1);
//			c1 = System.currentTimeMillis();
//			total += c1 - c0;
//		}

		for(int stageNum = 4; stageNum < 2 * 4 + Global.MAX_ITER - 1; stageNum++) {
			c0 = System.currentTimeMillis();
			if(stage[1] != 0) {
//				System.out.println("Thread0 stageNum: " + stageNum);
				for(Step step : steps) {
					step.work();
				}
			}
			for(int index = 3; index>= 1; --index)
				stage[index] = stage[index - 1];
			if(stageNum == (Global.MAX_ITER - 1 + 4 )) {
				stage[0] = 0;
			}
			c1 = System.currentTimeMillis();
			cal += c1 - c0;
			barrier.workerSync(1);
			c1 = System.currentTimeMillis();
			total += c1 - c0;
		}
		long t1 = System.currentTimeMillis();
		System.out.println("thread_1: cal_time, " + cal + " total_time, " + total + "\n");
		try {
			fos = new FileOutputStream("thread 1's CALRATIO.txt");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			String txtfw = "cal_time\t" + cal + "\ntotal_time\t" + total + "\n";
			bos.write(txtfw.getBytes(), 0, txtfw.getBytes().length);
			bos.flush();
			bos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
