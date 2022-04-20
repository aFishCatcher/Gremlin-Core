package dml.gremlin.myThreadPool;

import java.util.ArrayList;

public class DataList<E> extends ArrayList<TaskDataBuffer<E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isEnd = false;

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
}
