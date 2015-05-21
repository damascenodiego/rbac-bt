package com.usp.icmc.labes.fsm;

import java.util.ArrayList;
import java.util.List;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmElement;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmModel {

	List<FsmElement> element;
	String name; 

	public FsmModel(String n) {
		this.element = new ArrayList<FsmElement>();
		name = n;
	}

	public List<FsmElement> getStates() {
		List<FsmElement> result = new ArrayList<FsmElement>();

		for (FsmElement el : this.element){
			if(el instanceof FsmState) result.add(el);
		}
		return result;
	}

	public List<FsmElement> getTransitions() {
		List<FsmElement> result = new ArrayList<FsmElement>();

		for (FsmElement el : this.element){
			if(el instanceof FsmTransition) result.add(el);
		}
		return result;
	}
	
	public void addFsmElement(FsmElement el){
		element.add(el);
	}
}
