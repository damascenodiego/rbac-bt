package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmModel {

	List<FsmState> states;
	List<FsmTransition> transitions;
	String name; 

	public FsmModel(String n) {
		this.states = new ArrayList<FsmState>();
		this.transitions = new ArrayList<FsmTransition>();
		name = n;
	}

	public synchronized List<FsmState> getStates() {
		return this.states;
	}

	public synchronized List<FsmTransition> getTransitions() {
		return this.transitions;
	}

	public synchronized void addState(FsmState el){
		if(!this.states.contains(el))
			this.states.add(el);
	}

	public synchronized void addTransition(FsmTransition el){
		if(!states.contains(el.getFrom())) 
			states.add(el.getFrom());
		
		if(!states.contains(el.getTo())) 
			states.add(el.getTo());

		if(!this.transitions.contains(el))
			this.transitions.add(el);
	}

	public synchronized String getName() {
		return name;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((states == null) ? 0 : states.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
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
