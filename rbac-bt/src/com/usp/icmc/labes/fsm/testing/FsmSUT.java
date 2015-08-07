package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmSUT {
	
	private FsmModel sut;
	private FsmState currentState;
	private String lastInput;
	private String lastOutput;
	
	public FsmSUT(FsmModel f) {
		this.sut = f;
		this.currentState = f.getInitialState();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FsmSUT))
			return false;
		FsmSUT other = (FsmSUT) obj;
		if (currentState == null) {
			if (other.currentState != null)
				return false;
		} else if (!currentState.equals(other.currentState))
			return false;
		if (sut == null) {
			if (other.sut != null)
				return false;
		} else if (!sut.equals(other.sut))
			return false;
		return true;
	}
	
	public FsmState getCurrentState() {
		return currentState;
	}
	
	public String getLastInput() {
		return lastInput;
	}
	
	public String getLastOutput() {
		return lastOutput;
	}
	
	public FsmModel getSut() {
		return sut;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentState == null) ? 0 : currentState.hashCode());
		result = prime * result + ((sut == null) ? 0 : sut.hashCode());
		return result;
	}

	public String input(String in){
		for (int i = 0; i < currentState.getOut().size(); i++) {
			FsmTransition tr = currentState.getOut().get(i);
			if(tr.getInput().equals(in)){
				currentState = tr.getTo();
				lastInput = tr.getInput();
				lastOutput = tr.getOutput();
				return tr.getOutput();
			}
		}
		return "";
	}
	
	public FsmTransition inputReturnsFsmTransition(String in){
		for (int i = 0; i < currentState.getOut().size(); i++) {
			FsmTransition tr = currentState.getOut().get(i);
			if(tr.getInput().equals(in)){
				currentState = tr.getTo();
				lastInput = tr.getInput();
				lastOutput = tr.getOutput();
				return tr;
			}
		}
		return null;
	}

	public void setCurrentState(FsmState currentState) {
		this.currentState = currentState;
	}
	
	
}
