package com.usp.icmc.labes.fsm.testing;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmElement;

public class FsmTestSuite extends FsmElement{
	
	List<FsmTestCase> testCases;
	String generatedBy;
	
	public FsmTestSuite() {
		super();
		testCases = new ArrayList<FsmTestCase>();
		generatedBy = "NONE";
	}
	
	public FsmTestSuite(String n) {
		super(n);
		testCases = new ArrayList<FsmTestCase>();
		generatedBy = "NONE";
	}
	
	
	public List<FsmTestCase> getTestCases() {
		return testCases;
	}
	
	public void setTestCases(List<FsmTestCase> testCases) {
		this.testCases = testCases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((testCases == null) ? 0 : testCases.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FsmTestSuite))
			return false;
		FsmTestSuite other = (FsmTestSuite) obj;
		if (testCases == null) {
			if (other.testCases != null)
				return false;
		} else if (!testCases.equals(other.testCases))
			return false;
		return true;
	}
	
	public String getGeneratedBy() {
		return generatedBy;
	}
	
	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}
}
