package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;

public class PairTestSimpleDissimilarity extends PairTestSimilarity {

	public PairTestSimpleDissimilarity(FsmTestCase tci, FsmTestCase tcj) {
		int nit = FsmTestCaseSimilarityUtils.getInstance().calcNdt(tci,tcj);
		double avgij = (tci.getPath().size()+tcj.getPath().size())/2.0;
		similarity = nit/avgij;
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else{
			this.test00 = tcj;
			this.test01 = tci;
		}
	}

}
