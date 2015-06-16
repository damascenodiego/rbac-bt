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

public class TestRbac2Fsm {

	private FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();
	private FsmUtils fsmUtils = FsmUtils.getInstance();

	@Test
	public void stateCover() {

		try {
			RbacPolicy massod = TestExample.create_Masood2010Example1();
			FsmModel fsmGenerated = fsmUtils.rbac2Fsm(massod);
			
			File fsmFile = new File("test/Masood2010Example1.fsm");
			File testSet = null;
			fsmFile.getParentFile().mkdirs();
			
			fsmUtils.WriteFsm(fsmGenerated, fsmFile);
			
			FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);
			
			assertEquals(fsmGenerated,fsm);
			
			FsmTestSuite qSet = testingUtils.stateCoverSet(fsm);
//			System.out.println(fsmFile.getName());
//			System.out.println("State Cover Set (Q)");
//			for (FsmTestCase s: qSet.getTestCases()) {
//				System.out.println(s.toString());
//			}
			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_qSet.test");
			testingUtils.WriteFsmTestSuite(qSet, testSet);
			
			assertEquals(qSet,testingUtils.LoadFsmTestSuiteFromFile(testSet));
			
			FsmTestSuite pSet = testingUtils.transitionCoverSet(fsm);
//			System.out.println("Transition Cover Set (T)");
//			for (FsmTestCase s: pSet.getTestCases()) {
//				System.out.println(s.toString());
//			}
			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_pSet.test");
			testingUtils.WriteFsmTestSuite(pSet, testSet);
			
			assertEquals(pSet,testingUtils.LoadFsmTestSuiteFromFile(testSet));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRbac2Fsm() {

		try {
			RbacPolicy massod = TestExample.create_Masood2010Example1();
			FsmModel fsmGenerated = fsmUtils.rbac2Fsm(massod);
			FsmModel fsmConcurrentGenerated = fsmUtils.rbac2FsmConcurrent(massod);
			
			
			FsmModel[] models = {fsmGenerated, fsmConcurrentGenerated};
			for (FsmModel m : models) {
				Collections.sort(m.getStates(), new Comparator<FsmState>() {
					@Override
					public int compare(FsmState o1, FsmState o2) {
						return o1.toString().compareTo(o2.toString());
					}
				});
				
				Collections.sort(m.getTransitions(), new Comparator<FsmTransition>() {
					@Override
					public int compare(FsmTransition o1, FsmTransition o2) {
						return o1.toString().compareTo(o2.toString());
					}
				});
				
				Collections.sort(m.getInputs(), new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				
				Collections.sort(m.getOutputs(), new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
			}

			assertEquals(fsmGenerated,fsmConcurrentGenerated);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
