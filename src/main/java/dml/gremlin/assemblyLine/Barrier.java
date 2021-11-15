package dml.gremlin.assemblyLine;

public class Barrier {
	//注释
	volatile int[] barrierBuffer;
	public void masterSync() {
		int n = barrierBuffer.length;
		int i, sum;
		do {
			for(i = 1, sum = 1; i < n; i++)
				sum += barrierBuffer[i];
		} while(sum < n);
		for(i = 1; i < n; i++)
			barrierBuffer[i] = 0;
	}
	
	public void workerSync(int tid) {
		barrierBuffer[tid] = 1;
		while(barrierBuffer[tid]!=0);
	}
	
	public Barrier(int n) {
		barrierBuffer = new int[n];
		for(int i = 0; i < n; i++)
			barrierBuffer[i] = 0;
	}
	
}
