package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.utils.RandomGenerator;

public abstract class PairTestSimilarity  implements Comparable<PairTestSimilarity>  {
	FsmTestCase test00;
	FsmTestCase test01;
	double similarity;
	
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
	
	public FsmTestCase getTestShorter() {
		return test00;
	}

	public FsmTestCase getTestLonger() {
		return test01;
	}

	public boolean hasEqualSize() {
		return test00.getPath().size()==test01.getPath().size();
	}

	@Override
	public int compareTo(PairTestSimilarity o) {
		return Double.compare(o.similarity, this.similarity);
	}

}
