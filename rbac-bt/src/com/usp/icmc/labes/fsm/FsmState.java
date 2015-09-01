package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmState extends FsmElement{

	private List<FsmTransition> in;
	private List<FsmTransition> out;
	
	public FsmState(int id_num) {
		super(id_num);
		in  = new ArrayList<FsmTransition>();
		out = new ArrayList<FsmTransition>();
	}
	
	
	
	public List<FsmTransition> getIn() {
		return in;
	}
	
	public List<FsmTransition> getOut() {
		return out;
	}

	@Override
	public String toString() {
		return Integer.toString(getId());
	}

}
