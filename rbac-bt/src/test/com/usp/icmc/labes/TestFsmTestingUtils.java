package test.com.usp.icmc.labes;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.List;

import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;

public class TestFsmTestingUtils {

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
			
			FsmTestSuite pSet = testingUtils.transitionCoverSet(fsm);
//			System.out.println("Transition Cover Set (T)");
//			for (FsmTestCase s: pSet.getTestCases()) {
//				System.out.println(s.toString());
//			}
			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_pSet.test");
			testingUtils.WriteFsmTestSuite(pSet, testSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
