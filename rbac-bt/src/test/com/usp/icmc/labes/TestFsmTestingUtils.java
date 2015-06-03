package test.com.usp.icmc.labes;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;

public class TestFsmTestingUtils {

	private FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();
	private FsmUtils fsmUtils = FsmUtils.getInstance();
	@Test
	public void stateCover() {

		try {
			File fsmFile = new File("policies/examples/Masood2010Example1.fsm");
			FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);
			List<FsmPath> qSet = testingUtils.stateCoverSet(fsm);
			System.out.println(fsmFile.getName());
			System.out.println("State Cover Set (Q)");
			for (FsmPath s: qSet) {
				System.out.println(s.toString());
			}
			
			List<FsmPath> pSet = testingUtils.transitionCoverSet(fsm);
			System.out.println("Transition Cover Set (T)");
			for (FsmPath s: pSet) {
				System.out.println(s.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
