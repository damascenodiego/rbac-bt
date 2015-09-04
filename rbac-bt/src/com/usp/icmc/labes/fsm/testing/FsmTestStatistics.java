package com.usp.icmc.labes.fsm.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class FsmTestStatistics{

	long noResets;
	long testSuiteLength;
	long testSuiteLengthNoResets;
	long medianLength;
	Long minLength;
	Long maxLength;
	double avgLength;
	double sdLength;
	double varLength;
	


	public FsmTestStatistics(FsmTestSuite test){

		noResets = test.getTestCases().size();
		testSuiteLength = calcTestSuiteLength(test);
		testSuiteLengthNoResets = calcTestSuiteLength(test) - noResets;
		avgLength = calcAvg(test);
		varLength = calcVariance(test);
		sdLength = calcStdDev();
		calcMedianMinMax(test);
	}   


	public FsmTestStatistics(FsmTestSuiteIterator testIter){
		try {
			testIter.openFile();
			while(testIter.hasNextTestCase()){
				FsmTestCase tc = testIter.nextTestCase();
				noResets++;
				testSuiteLength += tc.getPath().size()+1; //+1 reset
				testSuiteLengthNoResets += tc.getPath().size();
			}
			testIter.reset();
			avgLength = getTestSuiteLength()/getNoResets();

			List<Long> data = new ArrayList<>();
			while(testIter.hasNextTestCase()){
				FsmTestCase tc = testIter.nextTestCase();
				long tcSize = tc.getPath().size();
				data.add(tcSize);
				varLength += (getAvgLength()-tcSize)*(getAvgLength()-tcSize);
			}
			Collections.sort(data);
			minLength = Collections.min(data);
			maxLength = Collections.max(data);
			if (data.size()% 2 == 0) medianLength = (long) ((data.get((data.size() / 2) - 1) + data.get(data.size()/ 2) / 2.0));
			else medianLength = data.get((int) ((data.size()/ 2) / 2.0));
						
			varLength = getVarLength() / getNoResets();  
			sdLength = calcStdDev();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}   

	long calcTestSuiteLength(FsmTestSuite testSuite) {
		long tsLength = 0;
		for(FsmTestCase t : testSuite.getTestCases()){
			tsLength += t.getPath().size()+1; //+1 reset
		}
		return tsLength;
	}


	double calcAvg(FsmTestSuite testSuite){
		double sum = 0.0;
		double size = testSuite.getTestCases().size();
		for(FsmTestCase t : testSuite.getTestCases()){
			double a = t.getPath().size();
			sum += a;
		}
		return sum/size;
	}

	double calcVariance(FsmTestSuite testSuite){
		double mean = getAvgLength();
		double temp = 0;
		double size = testSuite.getTestCases().size();
		for(FsmTestCase t : testSuite.getTestCases()){
			double a = t.getPath().size();
			temp += (mean-a)*(mean-a);
		}
		return temp/size ;
	}

	double calcStdDev()
	{
		return Math.sqrt(getVarLength());
	}

	void calcMedianMinMax(FsmTestSuite testSuite){
		List<Long> data = new ArrayList<>();
		for (FsmTestCase tc: testSuite.getTestCases()) {
			data.add((long) tc.getPath().size());
		}
		Collections.sort(data);
		minLength = Collections.min(data);
		maxLength = Collections.max(data);

		if (data.size()% 2 == 0) medianLength = (long) ((data.get((data.size() / 2) - 1) + data.get(data.size()/ 2) / 2.0));
		else medianLength = data.get((int) ((data.size()/ 2) / 2.0));
	}


	public long getNoResets() {
		return noResets;
	}


	public long getTestSuiteLength() {
		return testSuiteLength;
	}


	public long getTestSuiteLengthNoResets() {
		return testSuiteLengthNoResets;
	}


	public long getMinLength() {
		return minLength;
	}


	public long getMaxLength() {
		return maxLength;
	}


	public double getAvgLength() {
		return avgLength;
	}


	public double getSdLength() {
		return sdLength;
	}


	public double getVarLength() {
		return varLength;
	}


	public void setNoResets(long noResets) {
		this.noResets = noResets;
	}


	public void setTestSuiteLength(long testSuiteLength) {
		this.testSuiteLength = testSuiteLength;
	}


	public void setTestSuiteLengthNoResets(long testSuiteLengthNoResets) {
		this.testSuiteLengthNoResets = testSuiteLengthNoResets;
	}


	public void setMinLength(long minLength) {
		this.minLength = minLength;
	}


	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}


	public void setAvgLength(double avgLength) {
		this.avgLength = avgLength;
	}


	public void setSdLength(double sdLength) {
		this.sdLength = sdLength;
	}


	public void setVarLength(double varLength) {
		this.varLength = varLength;
	}


}