package com.usp.icmc.labes.fsm;

public class FsmTransition {
	
	private String input;
	private String output;
	
	private FsmState from;
	private FsmState to;
	
	public FsmTransition(FsmState f, FsmState t, String in, String out) {
		from = f;
		to = t;
		input = in;
		output = out;
	}
	
	public FsmState getFrom() {
		return from;
	}
	
	public FsmState getTo() {
		return to;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		FsmTransition other = (FsmTransition) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}


}
