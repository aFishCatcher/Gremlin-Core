package dml.gremlin.assemblyLine;

public class Barrier {
	//关于volatile修饰数组能否保证数组成员可见性的问题:
	//https://stackoverflow.com/questions/53753792/java-volatile-array-my-test-results-do-not-match-the-expectations
	//用了volatile后可能会出现缓冲区刷新次数太多的问题
	private final int len;
	private volatile boolean[] barrierBuffer;

	public Barrier(int n) {
		len = n-1;
		barrierBuffer = new boolean[len];
	}
	
	public void masterSync() {
		if(len<1) return;
		boolean result = true;
		do {
			result = true;
			for(boolean ele:barrierBuffer)
				result&=ele;
		}
		while(!result);
		
		for(int i=0; i<len; i++)
			barrierBuffer[i] = false;
	}
	
	public void workerSync(int tid) {
		int index = tid - 1;
		barrierBuffer[index] = true;
		while(barrierBuffer[index]);
	}
	
	
	
}
