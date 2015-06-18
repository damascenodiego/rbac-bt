package test.com.usp.icmc.labes;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.PolicyUnderTestUtils;

public class TestRbac2Fsm {

	private FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();
	private FsmUtils fsmUtils = FsmUtils.getInstance();
	private PolicyUnderTestUtils putUtils = PolicyUnderTestUtils.getInstance();

	@Test
	public void stateCoverAndTransitionCover() {

		try {
			RbacPolicy massod = putUtils.create_Masood2010Example1();
			FsmModel fsmGenerated = fsmUtils.rbac2Fsm(massod);
			
			File fsmFile = new File("test/Masood2010Example1.fsm");
			File testSet = null;
			fsmFile.getParentFile().mkdirs();
			
			fsmUtils.WriteFsm(fsmGenerated, fsmFile);
			
			FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);
			
			assertEquals(fsmGenerated,fsm);
			
			FsmTestSuite qSet = testingUtils.stateCoverSet(fsm);

			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_qSet.test");
			testingUtils.WriteFsmTestSuite(qSet, testSet);
			
			assertEquals(qSet,testingUtils.LoadFsmTestSuiteFromFile(testSet));
			assertEquals(qSet.getTestCases().size(),fsm.getStates().size());
			
			FsmTestSuite pSet = testingUtils.transitionCoverSet(fsm);

			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_pSet.test");
			testingUtils.WriteFsmTestSuite(pSet, testSet);
			
			assertEquals(pSet,testingUtils.LoadFsmTestSuiteFromFile(testSet));
			assertEquals(pSet.getTestCases().size(),fsm.getStates().size()*fsm.getInputs().size());			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRbac2Fsm() {

		List<RbacPolicy> pols = putUtils.getSmallPoliciesUnderTest();
		
		for (RbacPolicy rbacPolicy : pols) {
			try {
				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(rbacPolicy);
				FsmModel fsmConcurrentGenerated = fsmUtils.rbac2FsmConcurrent(rbacPolicy);
				
				fsmUtils.sorting(fsmGenerated);
				fsmUtils.sorting(fsmConcurrentGenerated);

				assertEquals(fsmGenerated,fsmConcurrentGenerated);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
