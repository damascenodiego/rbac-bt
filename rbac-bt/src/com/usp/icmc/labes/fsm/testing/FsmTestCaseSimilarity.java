package com.usp.icmc.labes.fsm.testing;

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
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.RandomGenerator;

public class FsmTestCaseSimilarity {

	private static FsmTestCaseSimilarity instance;

	private FsmTestCaseSimilarity(){ }

	public static FsmTestCaseSimilarity getInstance() {
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

	public static void main(String[] args) {
		File testCnfFile = new File(args[0]);
		try {
			List<RbacTestConfiguration> testCfgs = testingUtils.loadRbacTestConfiguration(testCnfFile);
			for (RbacTestConfiguration testconf : testCfgs) {
				if(!testconf.getTestConfigurationType().equals(RbacTestConfiguration.ConfigurationType.TEST_EXECUTION)) continue;
				for (FsmTestSuite tsuite : testconf.getTestSuites()) {
					sortSimilarityCartaxo(testconf.getRbacSpecification(),tsuite);
					testingUtils.WriteFsmTestSuiteAsKK(tsuite, new File("./"+testconf.getRbacSpecification().getName()+"."+tsuite.getGeneratedBy()+".txt"));
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class PairTestSimilarity implements Comparable<PairTestSimilarity>{

	FsmTestCase test00;
	FsmTestCase test01;
	double similarity;

	public PairTestSimilarity(FsmTestCase tci, FsmTestCase tcj) {
		int nit = FsmTestCaseSimilarity.getInstance().calcNit(tci,tcj);
		double avgij = (tci.getPath().size()+tcj.getPath().size())/2.0;
		similarity = nit/avgij;
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else if(tci.getPath().size()>tcj.getPath().size()){
			this.test00 = tcj;
			this.test01 = tci;
		}else if(RandomGenerator.getInstance().getRnd().nextBoolean()){
			this.test00 = tci;
			this.test01 = tcj;

		}else{
			this.test00 = tcj;
			this.test01 = tci;

		}
	}
	public FsmTestCase getTestShorter() {
		return test00;
	}

	public FsmTestCase getTestLonger() {
		return test01;
	}

	public boolean hasEqualSize() {
		return test00.getPath().size()==test01.getPath().size();
	}

	public FsmTestCase getTestRandom() {
		if(RandomGenerator.getInstance().getRnd().nextBoolean()){
			return test00;
		}
		return test01;
	}

	public FsmTestCase getTest00() {
		return test00;
	}

	public FsmTestCase getTest01() {
		return test01;
	}

	public double getSimilarity() {
		return similarity;
	}
	@Override
	public int compareTo(PairTestSimilarity o) {
		return Double.compare(o.similarity, this.similarity);
	}
}