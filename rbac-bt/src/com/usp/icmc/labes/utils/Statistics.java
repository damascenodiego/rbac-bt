package com.usp.icmc.labes.utils;

import java.util.Arrays;

public class Statistics{
	double[] data;
	int size;   

	Double mean;
	Double median;
	Double variance;
	Double stdDev;
	Double max;
	Double min;

	public Statistics(double[] data) 
	{
		this.data = data;
		size = data.length;
		mean = calcMean();
		median = calcMedian();
		variance = calcVariance();
		stdDev = calcStdDev();
		max = calcMax();
		min = calcMin();
	}   

	double calcMean()
	{
		double sum = 0.0;
		for(double a : data)
			sum += a;
		return sum/size;
	}

	double calcVariance()
	{
		double mean = getMean();
		double temp = 0;
		for(double a :data)
			temp += (mean-a)*(mean-a);
		return temp/size;
	}

	double calcStdDev()
	{
		return Math.sqrt(getVariance());
	}

	double calcMedian() 
	{
		Arrays.sort(data);

		if (data.length % 2 == 0) 
		{
			return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
		} 
		else 
		{
			return data[data.length / 2];
		}
	}
	double calcMax(){
		for (double d : data) {
			if(max == null || max < d) max = d;
		}
		return max;
	}
	double calcMin(){
		for (double d : data) {
			if(min == null || min > d) min = d;
		}
		return min;
	}

	public double[] getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

	public Double getMean() {
		return mean;
	}

	public Double getMedian() {
		return median;
	}

	public Double getVariance() {
		return variance;
	}

	public Double getStdDev() {
		return stdDev;
	}
	public Double getMax() {
		return max;
	}
	public Double getMin() {
		return min;
	}
}