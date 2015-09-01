package com.usp.icmc.labes.fsm.testing;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmElement;

public class FsmTestSuite extends FsmElement{
	
	List<FsmTestCase> testCases;
	String name;
	String generatedBy;
	
	public FsmTestSuite() {
		super();
		testCases = new ArrayList<FsmTestCase>();
		generatedBy = "NONE";
	}
	
	public FsmTestSuite(String n) {
		this();
		generatedBy = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public List<FsmTestCase> getTestCases() {
		return testCases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((testCases == null) ? 0 : testCases.hashCode());
		return result;
	}
	
	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}
	
	public void setTestCases(List<FsmTestCase> testCases) {
		this.testCases = testCases;
	}
}
