package dml.costream.lib;

public abstract class RunCOStream {
	private int outputNum;
	
	public abstract void Run(int srcDataCount);
	
	public int GetOutputNum() {
		return outputNum;
	}
}
