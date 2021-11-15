package dml.gremlin.assemblyLine;

import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.Step;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

/*
 *  worker是一个生产者/消费者节点的抽象
 *  包含中间的计算单元以及上下游缓冲区
 */
public class Worker {
	
	private final Compute<Traverser, List<Traverser>> computer;  //计算单元, step实现该接口
	private final Buffer<Traverser> up;  //上游缓冲区
	private final Buffer<Traverser> down;  //下游缓冲区
	
	private final boolean isSource;  //是否是数据源
	private final int stageNum;  
	
	public Worker(Compute _computer, Buffer _up, Buffer _down, int _stageNum) {
		this.computer = _computer;
		this.up = _up;
		this.down = _down;
		this.isSource = false;
		stageNum = _stageNum;
	}
	public Worker(Compute _computer, Buffer _up, Buffer _down, boolean _isSource, int _stageNum) {
		this.computer = _computer;
		this.up = _up;
		this.down = _down;
		this.isSource = _isSource;
		stageNum = _stageNum;
	}
	
	public int getStageNum() {
		return this.stageNum;
	}
	
//	public void work() {
//		computer.compute(up, down);
//	}
	
	public void work(){
		int output_count = 0;
		if(isSource) {
			List<Traverser> list = computer.compute(null);
			if(list!=null) {
				for(Traverser t : list) {
					down.putData(t);
					output_count++;
				}
			}
			down.putNum(output_count);
		}
		else {
			int rec_count = up.takeNum();
			for(int i=0; i<rec_count; i++) {
				Traverser traverser = up.takeData();
				List<Traverser> list = computer.compute(traverser);
				if(list!=null) {
					for(Traverser t : list) {
						down.putData(t);
						output_count++;
					}
				}
			}
			down.putNum(output_count);
		}
	}
}
