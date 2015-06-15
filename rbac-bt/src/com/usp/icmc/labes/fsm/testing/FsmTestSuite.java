package com.usp.icmc.labes.fsm.testing;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmElement;

public class FsmTestSuite extends FsmElement{
	
	List<FsmTestCase> testCases;
	
	public FsmTestSuite() {
		super();
		testCases = new ArrayList<FsmTestCase>();
	}
	
	public FsmTestSuite(String n) {
		super(n);
		testCases = new ArrayList<FsmTestCase>();
	}
	
	
	public List<FsmTestCase> getTestCases() {
		return testCases;
	}
	
	public void setTestCases(List<FsmTestCase> testCases) {
		this.testCases = testCases;
	}

}
