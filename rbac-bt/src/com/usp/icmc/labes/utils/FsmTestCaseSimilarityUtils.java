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
import com.usp.icmc.labes.fsm.testing.PairTestRbacSimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimpleDissimilarity;
import com.usp.icmc.labes.fsm.testing.PairTestSimpleSimilarity;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration.ConfigurationType;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.model.RbacPolicy;

public class FsmTestCaseSimilarityUtils {

	private static FsmTestCaseSimilarityUtils instance;

	private FsmTestCaseSimilarityUtils(){ }

	public static FsmTestCaseSimilarityUtils getInstance() {
		if(instance==null) instance = new FsmTestCaseSimilarityUtils();
		return instance;
	}


	public void sortSimilarityCartaxo(FsmModel spec, FsmTestSuite testSuite) {
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

		List<PairTestSimilarity> distMatrix = generateDistMatrix(testList);

		List<FsmTestCase> l = sortBertolinoAlgorithm(distMatrix,testList);
		testSuite.getTestCases().clear();
		testSuite.getTestCases().addAll(l);
	}

	public void sortSimilarityDamasceno(RbacPolicy pol, FsmModel spec, FsmTestSuite testSuite) {
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

		List<PairTestSimilarity> distMatrix = generateDistMatrixDamasceno(pol,testList);

		List<FsmTestCase> l = sortBertolinoAlgorithm(distMatrix,testList);
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
			distMatrix.remove(gtSim);
			List<PairTestSimilarity> tmp = new ArrayList<PairTestSimilarity>();
			Map<PairTestSimilarity,FsmTestCase> tmpMap = new HashMap<PairTestSimilarity,FsmTestCase>();
			while (!s.isEmpty()) {
				tmp.clear();
				for (FsmTestCase tcL : l) {
					for (FsmTestCase tcS : s) {
						for (PairTestSimilarity pair : distMatrix) {
							if(pair.hasTest(tcL) && pair.hasTest(tcS)){
								if(!tmp.contains(pair)){
									tmp.add(pair);
									tmpMap.put(pair, tcS);
								}
								break;
							}
						}
					}
				}	
				Collections.sort(tmp);
				PairTestSimilarity selPair = tmp.get(0);
				FsmTestCase selTest = tmpMap.get(selPair);
				if(!l.contains(selTest)){
					l.add(selTest);
					s.remove(selTest);
				}
				distMatrix.remove(selPair);
	//			removePairsWithTestCase(distMatrix, selTest);
				if(s.size() == 2 ){
					l.add(s.get(0));
					l.add(s.get(1));
					s.remove(0);
					s.remove(0);
				}
			}
			return l;
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
				pairsTest.add(new PairTestSimpleDissimilarity(tci, tcj));
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

	public int calcNdt(FsmTestCase tci, FsmTestCase tcj) {
		Set<FsmTransition> it = new HashSet<FsmTransition>();
		it.addAll(tci.getPath());
		it.retainAll(tcj.getPath());

		Set<FsmTransition> dt = new HashSet<FsmTransition>();
		dt.addAll(tci.getPath());
		dt.addAll(tcj.getPath());
		dt.removeAll(it);
		return dt.size();
	}

	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();

	public void sortSimilarityRandom(FsmTestSuite testSuite) {
		Collections.shuffle(testSuite.getTestCases(),RandomGenerator.getInstance().getRnd());
	}

}