package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmState extends FsmElement{

	private List<FsmTransition> in;
	private List<FsmTransition> out;
	
	public FsmState(String n) {
		super(n);
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((out == null) ? 0 : out.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
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
		return true;
	}

	
}
