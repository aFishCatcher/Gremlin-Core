package dml.costream.lib;

public class NumPtr {
	private double value;
	
	public NumPtr() {
		this.value = 0;
	}
	public NumPtr(double value) {
		this.value = value;
	}
	
	public int getInt() {
		return (int)value;
	}
	public long getLong() {
		return (long)value;
	}
	public float getFloat() {
		return (float)value;
	}
	public double getDouble() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString() {
		return "" + value;
	}
}
