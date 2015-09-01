package com.usp.icmc.labes.fsm;

public class FsmTransition extends FsmElement{

	private FsmState from;

	private String input;
	private String output;
	
	private FsmState to;
	
	public FsmTransition(){
		super();
	}
	
	public FsmTransition(FsmState f, String in, String out, FsmState t) {
		this();
		from = f;
		to = t;
		input = in;
		output = out;
		f.getOut().add(this);
		t.getIn().add(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
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
	
	public FsmState getFrom() {
		return from;
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public FsmState getTo() {
		return to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	public void setFrom(FsmState from) {
		this.from = from;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setTo(FsmState to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return from+" -- "+input+" / "+output+" -> "+to;
	}

}
