package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;

public class FsmPath extends FsmElement{

	private List<FsmTransition> paths;

	public FsmPath() {
		this.paths = new ArrayList<FsmTransition>();
	}
	public FsmPath(String n) {
		super(n);
		this.paths = new ArrayList<FsmTransition>();
	}

	public List<FsmTransition> getPath() {
		return paths;
	}

	public void setPath(List<FsmTransition> paths) {
		this.paths = paths;
	}

	public FsmState getInitialState(){
		if(this.paths.size()>0) return this.paths.get(0).getFrom();
		return null;
	}

	public FsmState getFinalState(){
		if(this.paths.size()>0) return this.paths.get(this.paths.size()-1).getTo();
		return null;
	}

	public void addTransition(FsmTransition t){
		getPath().add(t);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((paths == null) ? 0 : paths.hashCode());
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
		FsmPath other = (FsmPath) obj;
		if (paths == null) {
			if (other.paths != null)
				return false;
		} else if (!paths.equals(other.paths))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String out = getName();
		if(this.paths.size()<1) 
			return out +": EMPTY";
		out += ": "+getInitialState().getName();
		FsmTransition tr;
		for (int i = 0; i < paths.size(); i++) {
			tr = paths.get(i);
			out += " -- "+tr.getInput()+"/"+tr.getOutput()+" -> "+tr.getTo();
		}
		return out;
	}
}
