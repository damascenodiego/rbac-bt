package com.usp.icmc.labes.fsm.testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.util.List;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.RandomGenerator;

public class FsmTestCaseSimilarity {

	FsmTestCaseSimilarity instance;

	private FsmTestCaseSimilarity(){ }

	public FsmTestCaseSimilarity getInstance() {
		if(instance==null) instance = new FsmTestCaseSimilarity();
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

		double [][] distMatrix = generateDistMatrix(testList);

		double dist_ij = 0; 

		List<Integer> s = new ArrayList<Integer>();
		for (int k = 0; k < testList.size(); k++)  s.add(k);

		List<Integer> l = new ArrayList<Integer>();
		while (true) {
			if(s.isEmpty() || s.size() == 1){
				l.add(s.get(0));
				s.remove(0);
				break;
			}
			double greaterDist = -1;
			int greater_i = -1;
			int greater_j = -1;
			for (int k = 0; k < s.size()-1; k++) {
				for (int k2 = k+1; k2 < s.size(); k2++) {
					dist_ij = getDist(s.get(k),s.get(k2),distMatrix);
					if(dist_ij>=greaterDist){
						greaterDist = dist_ij;
						greater_i = (k);
						greater_j = (k2);
					}
				}
			}
			FsmTestCase tc_k = testList.get(s.get(greater_i));
			FsmTestCase tc_k2 = testList.get(s.get(greater_j));
			if(tc_k.getPath().size()>tc_k2.getPath().size()){
				l.add(s.get(greater_j));
				s.remove(greater_j);
			}else if(tc_k.getPath().size()<tc_k2.getPath().size()){
				l.add(s.get(greater_i));
				s.remove(greater_i);
			}else if(RandomGenerator.getInstance().getRnd().nextBoolean()){
				l.add(s.get(greater_i));
				s.remove(greater_i);
			}else{
				l.add(s.get(greater_j));
				s.remove(greater_j); 
			}
			System.out.println(greaterDist);
		}
		testSuite.getTestCases().clear();
		for (int i = l.size()-1; i >= 0; i--) {
			int index = l.get(i);
			testSuite.getTestCases().add(testList.get(index));
		}
		System.out.println(calcNit(testSuite.getTestCases().get(0), testSuite.getTestCases().get(1)));
	}

	private static double getDist(int i, int j, double[][] distMatrix) {
		int real_i = Math.min(i, j); 
		int max = Math.max(i, j);
		int real_j = max - real_i -1;
		return distMatrix[real_i][real_j];
	}

	private static double[][] generateDistMatrix(List<FsmTestCase> testList) {
		double [][] distMatrix = new double[testList.size()-1][];

		for (int i = 0; i < testList.size()-1; i++) {
			FsmTestCase tci = testList.get(i);
			distMatrix[i] = new double[testList.size()-i-1];
			for (int j = i+1; j < testList.size(); j++) {
				FsmTestCase tcj = testList.get(j);
				int nit = calcNit(tci,tcj);
				double avgij = (tci.getPath().size()+tcj.getPath().size())/2.0;
				double similarityij = nit/avgij;
				distMatrix[i][j-i-1] = similarityij;
			}
		}
		return distMatrix;
	}

	private static int calcNit(FsmTestCase tci, FsmTestCase tcj) {
		Set<FsmTransition> tcNit = new HashSet<FsmTransition>();
		tcNit.addAll(tci.getPath());
		tcNit.retainAll(tcj.getPath());
		return tcNit.size();
	}

	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();

	public static void main(String[] args) {
		File testCnfFile = new File(args[0]);
		try {
			List<RbacTestConfiguration> testCfgs = testingUtils.loadRbacTestConfiguration(testCnfFile);
			for (RbacTestConfiguration testconf : testCfgs) {
				for (FsmTestSuite tsuite : testconf.getTestSuites()) {
					sortSimilarityCartaxo(testconf.getRbacSpecification(),tsuite);
					testingUtils.WriteFsmTestSuiteAsKK(tsuite, new File("./test.txt"));
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
