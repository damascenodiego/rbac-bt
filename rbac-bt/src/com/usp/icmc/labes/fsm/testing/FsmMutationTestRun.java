package com.usp.icmc.labes.fsm.testing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmMutationTestRun{

	private Set<FsmSUT> fsmSuts;
	private FsmSUT specification;
	private FsmTestSuite testSuite;

	private List<FsmSUT> alivePolicies;
	private List<FsmSUT> killedPolicies;

	public FsmMutationTestRun(FsmModel fsmSpec,FsmTestSuite suite) {
		this.fsmSuts = new HashSet<FsmSUT>();
		this.alivePolicies = new ArrayList<FsmSUT>();
		this.killedPolicies = new ArrayList<FsmSUT>();
		this.specification = new FsmSUT(fsmSpec);
		this.testSuite = suite;
	}

	public FsmMutationTestRun addAllSut(List<FsmSUT> allSuts){
		for (FsmSUT sut : allSuts) {
			addSut(sut);
		}
		return this;
	}

	public FsmMutationTestRun addSut(FsmSUT sut){
		if(!sut.getSut().equals(specification)) {
			if(!this.fsmSuts.contains(sut)) this.fsmSuts.add(sut);
			if(!this.alivePolicies.contains(sut)) this.alivePolicies.add(sut);
		}
		return this;
	}

	public List<FsmSUT> getAlivePolicies() {
		return alivePolicies;
	}

	public Set<FsmSUT> getFsmSuts() {
		return fsmSuts;
	}

	public List<FsmSUT> getKilledPolicies() {
		return killedPolicies;
	}

	public FsmSUT getSpecification() {
		return this.specification;
	}

	public FsmTestSuite getTestSuite() {
		return this.testSuite;
	}

	private void resetAll() {
		for (FsmSUT sut : alivePolicies) {
			sut.setCurrentState(sut.getSut().getInitialState());
		}
		specification.setCurrentState(specification.getSut().getInitialState());
	}

	public FsmMutationTestRun setTestSuite(FsmTestSuite testSuite) {
		this.testSuite = testSuite;
		return this;
	}
	public boolean testInputWithOutput(){
		return testInputWithOutput(false);
	}

	public boolean testInputWithOutput(boolean reset){
		String in = null;
		String out = null;
		String mutOut = null;
		for (FsmTestCase tCase : testSuite.getTestCases()) {
			if(reset) resetAll();
			for (FsmTransition tr : tCase.getPath()) {
				in = tr.getInput();
				out = this.specification.input(in);
				mutOut = null;
				for (FsmSUT fsmSUT : alivePolicies) {
					if(killedPolicies.contains(fsmSUT)) continue;
					mutOut = fsmSUT.input(in);
					if(!mutOut.equals(out)){
						killedPolicies.add(fsmSUT);
					}
				}
			}
		}
		return alivePolicies.removeAll(killedPolicies);
	}

	public boolean testInputWithState(){
		return testInputWithState(false);
	}
	public boolean testInputWithState(boolean reset){
		String in = null;
		String out = null;
		String mutOut = null;
		FsmState sutState = null;
		FsmState specState = null;
		for (FsmTestCase tCase : testSuite.getTestCases()) {
			if(reset) resetAll();
			for (FsmTransition tr : tCase.getPath()) {
				in = tr.getInput();
				out = this.specification.input(in);
				specState = this.specification.getCurrentState();
				for (FsmSUT fsmSUT : alivePolicies) {
					mutOut = fsmSUT.input(in);
					sutState = fsmSUT.getCurrentState();
					if(specState.equals(sutState)) continue;
					killedPolicies.add(fsmSUT);
				}
			}
		}
		return alivePolicies.removeAll(killedPolicies);
	}
}
//			if(fsmSUT.getCurrentState().equals(fsmSUT.getCurrentState())){
//				killedPolicies.add(fsmSUT);
//			}
//		}
//		return alivePolicies.removeAll(killedPolicies);
//	}

