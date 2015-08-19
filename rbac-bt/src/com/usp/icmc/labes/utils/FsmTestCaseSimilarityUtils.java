package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.util.List;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmSUT;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.fsm.testing.PairTestSimilarity;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration.ConfigurationType;

public class FsmTestCaseSimilarityUtils {

	private static FsmTestCaseSimilarityUtils instance;

	private FsmTestCaseSimilarityUtils(){ }

	public static FsmTestCaseSimilarityUtils getInstance() {
		if(instance==null) instance = new FsmTestCaseSimilarityUtils();
		return instance;
	}


	public static void sortSimilarityCartaxo(FsmModel spec, FsmTestSuite testSuite) {
		FsmSUT sut = new FsmSUT(spec);
		FsmState initState = sut.getCurrentState();
		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());
		for (FsmTestCase testCase : testList) {
			for (FsmTransition tr: testCase.getPath()) {
				FsmTransition specTr = sut.inputReturnsFsmTransition(tr.getInput());
				tr.setFrom(specTr.getFrom());
				tr.setTo(specTr.getTo());
				tr.setOutput(specTr.getOutput());
			}
			sut.setCurrentState(initState);

		}

		//		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);
		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);

		double dist_ij = 0; 

		List<FsmTestCase> s = new ArrayList<FsmTestCase>();
		for (int k = 0; k < testList.size(); k++)  s.add(testList.get(k));
		Collections.sort(distMatrix);
		List<FsmTestCase> l = new ArrayList<FsmTestCase>();
//		for (PairTestSimilarity gtSim : distMatrix) {

//		}
//
		PairTestSimilarity gtSim = null;
		while (!distMatrix.isEmpty()) {
			gtSim = distMatrix.get(0);

			FsmTestCase selTest = null;
			if(gtSim.hasEqualSize()){
				selTest = gtSim.getTestRandom();
			}else selTest = gtSim.getTestLonger();
			removePairsWithTestCase(distMatrix,selTest);
			l.add(selTest);
			distMatrix.remove(gtSim);
			System.out.println(gtSim.getSimilarity());
		}
		if(!l.contains(gtSim.getTestLonger())) l.add(gtSim.getTestLonger());
		if(!l.contains(gtSim.getTestShorter())) l.add(gtSim.getTestShorter());
		
		testSuite.getTestCases().clear();
		testSuite.getTestCases().addAll(l);
	}

	public void sortSimilarityDamasceno(FsmModel spec, FsmTestSuite testSuite) {
		FsmSUT sut = new FsmSUT(spec);
		FsmState initState = sut.getCurrentState();
		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());
		for (FsmTestCase testCase : testList) {
			for (FsmTransition tr: testCase.getPath()) {
				FsmTransition specTr = sut.inputReturnsFsmTransition(tr.getInput());
				tr.setFrom(specTr.getFrom());
				tr.setTo(specTr.getTo());
				tr.setOutput(specTr.getOutput());
			}
			sut.setCurrentState(initState);

		}

		//		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);
		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);

		List<FsmTestCase> s = new ArrayList<FsmTestCase>();
		for (int k = 0; k < testList.size(); k++)  s.add(testList.get(k));
		Collections.sort(distMatrix);
		List<FsmTestCase> l = new ArrayList<FsmTestCase>();
//		for (PairTestSimilarity gtSim : distMatrix) {

//		}
//
		PairTestSimilarity gtSim = null;
		while (!distMatrix.isEmpty()) {
			gtSim = distMatrix.get(0);

			FsmTestCase selTest = null;
			if(gtSim.hasEqualSize()){
				selTest = gtSim.getTestRandom();
			}else selTest = gtSim.getTestLonger();
			removePairsWithTestCase(distMatrix,selTest);
			l.add(selTest);
			distMatrix.remove(gtSim);
			System.out.println(gtSim.getSimilarity());
		}
		if(!l.contains(gtSim.getTestLonger())) l.add(gtSim.getTestLonger());
		if(!l.contains(gtSim.getTestShorter())) l.add(gtSim.getTestShorter());
		
		testSuite.getTestCases().clear();
		testSuite.getTestCases().addAll(l);
	}

	private static void removePairsWithTestCase(List<PairTestSimilarity> distMatrix, FsmTestCase selTest) {
		Set<PairTestSimilarity> toRemove = new HashSet<PairTestSimilarity>();
		for (PairTestSimilarity pairTestSimilarity : distMatrix) {
			if(pairTestSimilarity.getTest01().equals(selTest) || pairTestSimilarity.getTest00().equals(selTest)) toRemove.add(pairTestSimilarity);
		}
		distMatrix.removeAll(toRemove);
	}

	public static double getDist(int i, int j, double[][] distMatrix) {
		int real_i = Math.min(i, j); 
		int max = Math.max(i, j);
		int real_j = max - real_i -1;
		return distMatrix[real_i][real_j];
	}

	//	private static double[][] generateDistMatrix(List<FsmTestCase> testList) {
	private static List<PairTestSimilarity> generateDistMatrix(List<FsmTestCase> testList) {
		//		double [][] distMatrix = new double[testList.size()-1][];
		List<PairTestSimilarity> pairsTest = new ArrayList<PairTestSimilarity>();

		for (int i = 0; i < testList.size()-1; i++) {
			FsmTestCase tci = testList.get(i);
			//			distMatrix[i] = new double[testList.size()-i-1];
			for (int j = i+1; j < testList.size(); j++) {
				FsmTestCase tcj = testList.get(j);
				//				int nit = calcNit(tci,tcj);
				//				double avgij = (tci.getPath().size()+tcj.getPath().size())/2.0;
				//				double similarityij = nit/avgij;
				//				distMatrix[i][j-i-1] = similarityij;
				pairsTest.add(new PairTestSimilarity(tci, tcj));
			}
		}
		return pairsTest;
	}

	public int calcNit(FsmTestCase tci, FsmTestCase tcj) {
		Set<FsmTransition> tcNit = new HashSet<FsmTransition>();
		tcNit.addAll(tci.getPath());
		tcNit.retainAll(tcj.getPath());
		return tcNit.size();
	}

	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();

}