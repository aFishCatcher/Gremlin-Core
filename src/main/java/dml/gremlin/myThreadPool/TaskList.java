package dml.gremlin.myThreadPool;

import java.util.ArrayList;

public class TaskList extends ArrayList<Task> {
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
