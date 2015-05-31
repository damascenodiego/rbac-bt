package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmModel extends FsmElement{

	List<FsmState> states;
	List<FsmTransition> transitions;

	public FsmModel(String n) {
		super(n);
		this.states = new ArrayList<FsmState>();
		this.transitions = new ArrayList<FsmTransition>();
	}

	public List<FsmState> getStates() {
		return this.states;
	}

	public FsmState getState(String name) {
		for (FsmState fsmState : states) {
			if(fsmState.getName().equals(name)) 
				return fsmState;
		}
		return null;
	}

	public FsmState getState(FsmState s) {
		for (FsmState fsmState : states) {
			if(fsmState.equals(s)) 
				return fsmState;
		}
		return null;
	}

	public List<FsmTransition> getTransitions() {
		return this.transitions;
	}

	public void addState(FsmState el){
		if(!this.states.contains(el))
			this.states.add(el);
	}

	public void addTransition(FsmTransition el){
		if(!this.transitions.contains(el)){
			this.transitions.add(el);
			addState(el.getFrom());
			addState(el.getTo());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((states == null) ? 0 : states.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
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
