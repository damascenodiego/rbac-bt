package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmTestStep extends FsmTransition {

	public FsmTestStep(FsmState f, String in, String out, FsmState t) {
		super(f, in, out, t);
	}

}
