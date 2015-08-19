package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;

public class PairTestSimpleSimilarity extends PairTestSimilarity {

	public PairTestSimpleSimilarity(FsmTestCase tci, FsmTestCase tcj) {
		int nit = FsmTestCaseSimilarityUtils.getInstance().calcNit(tci,tcj);
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