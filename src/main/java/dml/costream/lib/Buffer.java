package dml.costream.lib;

import java.util.concurrent.ConcurrentHashMap;

public class Buffer<E> {
	private int bufferSize;
	private int copySize;
	private int copyStartPos;
	private ConcurrentHashMap<Integer, E> buffer;
	
	public Buffer(int bufferSize, int copySize, int copyStartPos) {
		this.bufferSize = bufferSize;
		this.copySize = copySize;
		this.copyStartPos = copyStartPos;
		buffer = new ConcurrentHashMap<Integer, E>(bufferSize);
	}
	
	

}
