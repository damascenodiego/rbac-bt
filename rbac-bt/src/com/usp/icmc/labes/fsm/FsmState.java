package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmState extends FsmElement {

	public enum StateType{
		INITIAL_STATE,
		SIMPLE_STATE,
		FINAL_STATE,
	}

	private StateType type;
	
	private List<FsmTransition> in;
	private List<FsmTransition> out;
	
	public FsmState(String n,StateType t) {
		super(n);
		type = t;
	}
	public FsmState(String n) {
		super(n);
		in  = new ArrayList<FsmTransition>();
		out = new ArrayList<FsmTransition>();
		type = StateType.SIMPLE_STATE;
	}
	
	
	public List<FsmTransition> getIn() {
		return in;
	}
	
	public List<FsmTransition> getOut() {
		return out;
	}
	
	public StateType getType() {
		return type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((out == null) ? 0 : out.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FsmState))
			return false;
		FsmState other = (FsmState) obj;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
			return false;
		if (out == null) {
			if (other.out != null)
				return false;
		} else if (!out.equals(other.out))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
	
}
