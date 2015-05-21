package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmElement;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmModel {

	List<FsmState> states;
	List<FsmTransition> transitions;
	String name; 

	public FsmModel(String n) {
		this.states = new ArrayList<FsmState>();
		this.transitions = new ArrayList<FsmTransition>();
		name = n;
	}

	public List<FsmState> getStates() {
		return this.states;
	}

	public List<FsmTransition> getTransitions() {
		return this.transitions;
	}
	
	public void addState(FsmState el){
		this.states.add(el);
	}
	
	public void addTransition(FsmTransition el){
		if(!states.contains(el.getFrom())) states.add(el.getFrom());
		if(!states.contains(el.getTo())) states.add(el.getTo());
		
		this.transitions.add(el);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((states == null) ? 0 : states.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FsmModel))
			return false;
		FsmModel other = (FsmModel) obj;
		if (states == null) {
			if (other.states != null)
				return false;
		} else if (!states.equals(other.states))
			return false;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}
	
	
}
