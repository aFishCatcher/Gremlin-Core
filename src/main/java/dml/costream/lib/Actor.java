package dml.costream.lib;

public abstract class Actor {
	@SuppressWarnings("unused")
	private void initVarAndState() {}
	@SuppressWarnings("unused")
	private void init(){}
	@SuppressWarnings("unused")
	private void popToken() {}
	@SuppressWarnings("unused")
	private void pushToken() {}
	@SuppressWarnings("unused")
	private void initWork() {}
	@SuppressWarnings("unused")
	private void work() {}
	
	public abstract void runInitScheduleWork();
	public abstract void runSteadyScheduleWork();
}
