package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;

public class PairTestSimpleDissimilarity extends PairTestSimilarity {

	public PairTestSimpleDissimilarity(FsmTestCase tci, FsmTestCase tcj) {
		similarity = 1-FsmTestCaseSimilarityUtils.getInstance().calcJaccardSimilarity(tci,tcj);
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else{
			this.test00 = tcj;
			this.test01 = tci;
		}
	}

}
