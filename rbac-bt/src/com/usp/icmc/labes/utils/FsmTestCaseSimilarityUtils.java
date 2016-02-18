package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.util.List;
import java.util.Map;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmSUT;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.fsm.testing.PairTestDissimilarityNit;
import com.usp.icmc.labes.fsm.testing.PairTestRbacSimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimpleDissimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimpleSimilarity;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration.ConfigurationType;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class FsmTestCaseSimilarityUtils {

	private static FsmTestCaseSimilarityUtils instance;

	private FsmTestCaseSimilarityUtils(){ }

	public static FsmTestCaseSimilarityUtils getInstance() {
		if(instance==null) instance = new FsmTestCaseSimilarityUtils();
		return instance;
	}

	private static RbacUtils rbacUtils = RbacUtils.getInstance();

	public void sortSimilarityCartaxo(RbacPolicy pol, FsmTestSuite testSuite) {
		
		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());
		
		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);

		List<FsmTestCase> l = sortCartaxoAlgorithm(distMatrix);
		testSuite.getTestCases().clear();
		testSuite.getTestCases().addAll(l);
	}

	public void sortSimilarityDamasceno(RbacPolicy pol, FsmTestSuite testSuite) {
		
		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());

		List<PairTestSimilarity> distMatrix = generateDistMatrixDamasceno(pol,testList);

		List<FsmTestCase> l = sortCartaxoAlgorithm(distMatrix);
		testSuite.getTestCases().clear();
		testSuite.getTestCases().addAll(l);
	}

	private List<FsmTestCase> sortBertolinoAlgorithm(List<PairTestSimilarity> distMatrix, List<FsmTestCase> testList) {
		List<FsmTestCase> s = new ArrayList<FsmTestCase>();
		for (int k = 0; k < testList.size(); k++)  s.add(testList.get(k));
		Collections.sort(distMatrix);
		List<FsmTestCase> l = new ArrayList<FsmTestCase>();
		PairTestSimilarity gtSim = distMatrix.get(0);
		if(!l.contains(gtSim.getTestLonger())) 	l.add(gtSim.getTestLonger());
		if(!l.contains(gtSim.getTestShorter())) l.add(gtSim.getTestShorter());
		s.remove(gtSim.getTestLonger());
		s.remove(gtSim.getTestShorter());
//		distMatrix.remove(gtSim);
		Map<FsmTestCase,Double> tmpMap = new HashMap<FsmTestCase,Double>();
		while (!s.isEmpty()) {
			tmpMap.clear();
			for (FsmTestCase tcS : s) {
				tmpMap.put(tcS, 0.0);
				for (FsmTestCase tcL : l) {
					for (PairTestSimilarity pair : distMatrix) {
						if(pair.hasTest(tcL) && pair.hasTest(tcS)){
							tmpMap.put(tcS, tmpMap.get(tcS)+pair.getSimilarity());
						}
					}
				}
			}	
			FsmTestCase selTest = getMaxDist(tmpMap,s);
			l.add(selTest);
			s.remove(selTest);
			if(s.size() == 2 ){
				l.add(s.get(0));
				l.add(s.get(1));
				s.remove(0);
				s.remove(0);
			}
		}
		return l;
	}

	
	private List<FsmTestCase> sortCartaxoAlgorithm(List<PairTestSimilarity> distMatrix) {
		Collections.sort(distMatrix);
		List<FsmTestCase> l = new ArrayList<FsmTestCase>();
		Set<FsmTestCase> included = new HashSet<FsmTestCase>();
		for (PairTestSimilarity pairTestSimilarity : distMatrix) {
			if(!included.contains(pairTestSimilarity.getTestLonger())){
				l.add(pairTestSimilarity.getTestLonger());
				included.add(pairTestSimilarity.getTestLonger());
			}
			if(!included.contains(pairTestSimilarity.getTestShorter())){
				l.add(pairTestSimilarity.getTestShorter());
				included.add(pairTestSimilarity.getTestShorter());
			}
		}
		included.clear();
		return l;
	}

	private FsmTestCase getMaxDist(Map<FsmTestCase, Double> tmpMap, List<FsmTestCase> s) {
		double greater = 0.0;
		FsmTestCase greaterTc = s.get(s.size()-1);
		for (int i = s.size()-1; i >= 0; i--) {
			if (greater<=tmpMap.get(s.get(i))) {
				greater = tmpMap.get(s.get(i));
				greaterTc = s.get(i);
			}
		}
		return greaterTc;
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

	private static List<PairTestSimilarity> generateDistMatrix(List<FsmTestCase> testList) {
		List<PairTestSimilarity> pairsTest = new ArrayList<PairTestSimilarity>();

		for (int i = 0; i < testList.size()-1; i++) {
			FsmTestCase tci = testList.get(i);
			for (int j = i+1; j < testList.size(); j++) {
				FsmTestCase tcj = testList.get(j);
				pairsTest.add(new PairTestDissimilarityNit(tci, tcj));
			}
		}
		return pairsTest;
	}

	private List<PairTestSimilarity> generateDistMatrixDamasceno(RbacPolicy pol, List<FsmTestCase> testList) {
		List<PairTestSimilarity> pairsTest = new ArrayList<PairTestSimilarity>();
		RbacAcut acut = new RbacAcut(pol);
		for (int i = 0; i < testList.size()-1; i++) {
			FsmTestCase tci = testList.get(i);
			for (int j = i+1; j < testList.size(); j++) {
				FsmTestCase tcj = testList.get(j);
				pairsTest.add(new PairTestRbacSimilarity(tci, tcj, acut));
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

	public double calcNdtAvgLen(FsmTestCase tci, FsmTestCase tcj) {
		Set<FsmTransition> intersection = new HashSet<FsmTransition>();
		intersection.addAll(tci.getPath());
		intersection.retainAll(tcj.getPath());

		Set<FsmTransition> union = new HashSet<FsmTransition>();
		union.addAll(tci.getPath());
		union.addAll(tcj.getPath());
		union.removeAll(intersection);
		return union.size()/((tci.getPath().size()+tcj.getPath().size())/2.0);
	}

	public double calcJaccardSimilarity(FsmTestCase tci, FsmTestCase tcj) {
		Set<FsmTransition> intersection = new HashSet<FsmTransition>();
		intersection.addAll(tci.getPath());
		intersection.retainAll(tcj.getPath());

		Set<FsmTransition> union = new HashSet<FsmTransition>();
		union.addAll(tci.getPath());
		union.addAll(tcj.getPath());
		return intersection.size()/(double)union.size();
	}

	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();

	public void sortSimilarityRandom(FsmTestSuite testSuite) {
		Collections.shuffle(testSuite.getTestCases(),RandomGenerator.getInstance().getRnd());
	}

}