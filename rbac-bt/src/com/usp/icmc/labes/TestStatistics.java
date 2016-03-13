package com.usp.icmc.labes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class TestStatistics {

	private Map<String,Map<String,Map<Integer,List<Integer>>>> stats;
	private ArrayList<String> name;
	private ArrayList<String> meth;
	private ArrayList<Integer> fragment;

	public TestStatistics() {
		name = new ArrayList<String>();
		meth = new ArrayList<String>();
		fragment = new ArrayList<Integer>();
		stats = new HashMap<String,Map<String,Map<Integer,List<Integer>>>> ();
	}

	void addStatistics(String testName, String testPrtzMethod, int testPrtzFragment, int mutants){
		if(!name.contains(testName)) name.add(testName);
		if(!meth.contains(testPrtzMethod)) meth.add(testPrtzMethod);
		if(!fragment.contains(testPrtzFragment)) fragment.add(testPrtzFragment);

		stats.putIfAbsent(testName, new HashMap<String, Map<Integer,List<Integer>>> ());
		stats.get(testName).putIfAbsent(testPrtzMethod, new HashMap<Integer, List<Integer>>());
		stats.get(testName).get(testPrtzMethod).putIfAbsent(testPrtzFragment,new ArrayList<Integer>());
		stats.get(testName).get(testPrtzMethod).get(testPrtzFragment).add(mutants);
	}

	void sortIndex(){
		Collections.sort(name);
		Collections.sort(meth);
		Collections.sort(fragment);

	}

	public ArrayList<Integer> getFragment() {
		return fragment;
	}

	public ArrayList<String> getMeth() {
		return meth;
	}

	public ArrayList<String> getName() {
		return name;
	}

	public List<Integer> getStatistics(String testName, String testPrtzMethod, int testPrtzFragment){

		if (stats.containsKey(testName)) return null;
		if (stats.get(testName).containsKey(testPrtzMethod)) return null;
		if (stats.get(testName).get(testPrtzMethod).containsKey(testPrtzFragment)) return null;

		return stats.get(testName).get(testPrtzMethod).get(testPrtzFragment);
	}

	@Override
	public String toString() {
		Mean mean = new Mean();
		StringBuffer bw = new StringBuffer();

		for (String testName  : name) {
			bw.append("TestName\tPercent\t");
			for (String prtz : meth) bw.append(prtz+"\t");
			bw.append("\n");
			for (Integer perc  : fragment) {
				bw.append(testName); bw.append("\t");
				bw.append(Integer.toString(perc)); bw.append("\t");
				for (String prtz : meth) {
					if(perc == 100){
						double[] values = new double[stats.get(testName).get("NONE").get(100).size()];
						for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get("NONE").get(100).get(i);
						bw.append(Double.toString(mean.evaluate(values))+"\t");
					}else if(stats.get(testName).containsKey(prtz) && stats.get(testName).get(prtz).containsKey(perc)) {
						double[] values = new double[stats.get(testName).get(prtz).get(perc).size()];
						for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get(prtz).get(perc).get(i);
						bw.append(Double.toString(mean.evaluate(values))+"\t");
					} else{
						bw.append("-\t");
					}
				}
				bw.append("\n");
			}	
			bw.append("\n\n");

		}
		return super.toString();
	}



}

