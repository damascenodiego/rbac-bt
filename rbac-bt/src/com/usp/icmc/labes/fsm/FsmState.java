package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class FsmState {

	@XStreamAsAttribute
	private String name;

	private List<FsmTransition> in;
	private List<FsmTransition> out;
	
	public FsmState(String n) {
		name = n;
		in  = new ArrayList<FsmTransition>();
		out = new ArrayList<FsmTransition>();
	}
	
	
	public List<FsmTransition> getIn() {
		return in;
	}
	
	public List<FsmTransition> getOut() {
		return out;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FsmState other = (FsmState) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
