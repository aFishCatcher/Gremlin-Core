package dml.gremlin.general;

import java.util.ArrayList;

public class StepsList {
	public ArrayList<Step> steps = new ArrayList<Step> ();
	
	public StepsList() {
		
	}
	
	public void setStpes(ArrayList<Step> steps) {
		this.steps = steps;
	}
	
	public int size() {
		return steps.size();
	}
}
