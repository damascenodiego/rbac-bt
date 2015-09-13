package com.usp.icmc.labes.fsm.testing;

import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;

public class PairTestDissimilarityNit extends PairTestSimilarity {

	public PairTestDissimilarityNit(FsmTestCase tci, FsmTestCase tcj) {
		similarity = FsmTestCaseSimilarityUtils.getInstance().calcNdtAvgLen(tci,tcj);
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else{
			this.test00 = tcj;
			this.test01 = tci;
		}
	}

}
