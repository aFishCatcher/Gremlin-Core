package dml.gremlin.assemblyLine;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;

public class Buffer<T> {
	/*
	 * T is the data type
	 * @NotThreadSafe, 
	 * 在构造函数中将队列类型改为BlockingQueue委托线程安全性
	 */
	private final Queue<T> data;
	private final Queue<Integer> num;
	
	public Buffer() {
		data = new ArrayDeque<>();
		num = new ArrayDeque<>();
	}
	
	public void putData(T t) {
		data.add(t);
	}
	
	public void putNum(Integer n) {
		num.add(n);
	}
	
	public T takeData(){
		return data.remove();
	}
	
	public Integer takeNum(){
		return num.remove();
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
}
