package com.usp.icmc.labes.fsm.testing;

import java.util.Arrays;

public class FsmTestStatistics{

	FsmTestSuite testSuite;
	long noResets;
	long testSuiteLength;
	long testSuiteLengthNoResets;
	Long minLength;
	Long maxLength;
	double avgLength;
	double sdLength;
	double varLength;
	double medianLength;


	public FsmTestStatistics(FsmTestSuite test){
		testSuite = test;

		noResets = testSuite.getTestCases().size();
		testSuiteLength = calcTestSuiteLength();
		testSuiteLengthNoResets = calcTestSuiteLength() - noResets;
		avgLength = calcAvg();
		varLength = calcVariance();
		sdLength = calcStdDev();
		calcMedianMinMax();
	}   


	long calcTestSuiteLength() {
		long tsLength = 0;
		for(FsmTestCase t : testSuite.getTestCases()){
			tsLength += t.getPath().size()+1; //+1 reset
		}
		return tsLength;
	}


	double calcAvg(){
		double sum = 0.0;
		double size = testSuite.getTestCases().size();
		for(FsmTestCase t : testSuite.getTestCases()){
			double a = t.getPath().size();
			sum += a;
		}
		return sum/size;
	}

	double calcVariance(){
		double mean = getMedianLength();
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

	void calcMedianMinMax(){
		long [] data = new long[testSuite.getTestCases().size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = testSuite.getTestCases().get(i).getPath().size();
		}
		Arrays.sort(data);
		if (data.length % 2 == 0){
			medianLength = (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
		} 
		else{
			medianLength = data[data.length / 2];
		}
		for (long d : data) {
			if(maxLength == null || maxLength < d) maxLength = d;
			if(minLength == null || minLength > d) minLength = d;
		}
	}


	public FsmTestSuite getTestSuite() {
		return testSuite;
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


	public double getMedianLength() {
		return medianLength;
	}


	public void setTestSuite(FsmTestSuite testSuite) {
		this.testSuite = testSuite;
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


	public void setMedianLength(double medianLength) {
		this.medianLength = medianLength;
	}

}