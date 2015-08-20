package test.com.usp.icmc.labes;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.utils.CPP;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class TestFsmTesting {

	private static RbacAdministrativeCommands rbacAdmin	= RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys 	= RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils 					= RbacUtils.getInstance();
	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();
	private static FsmUtils fsmUtils 						= FsmUtils.getInstance();
	private static PolicyUnderTestUtils putUtils 			= PolicyUnderTestUtils.getInstance();

	public static void main(String[] args) {
		try {
			File fsmFile = new File("/home/damascenodiego/git/rbac-bt/rbac-bt/policies_example"
					+ "/Masood2009P1v2/Masood2009P1v2.fsm");
					
			FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);

			long ini = System.currentTimeMillis();
			CPP g = new CPP(fsm);
			List<FsmTransition> cptPath = g.getCPT();
			long end = System.currentTimeMillis();
			Set<FsmTransition> allTransitions = new HashSet<FsmTransition>();
			Set<FsmState> allStates = new HashSet<FsmState>();
			for (FsmTransition fsmTransition : cptPath) {
				if(!allStates.contains(fsmTransition.getTo())) allStates.add(fsmTransition.getTo());
//				if(!allStates.contains(fsmTransition.getFrom())) allStates.add(fsmTransition.getFrom());
				if(!allTransitions.contains(fsmTransition)) allTransitions.add(fsmTransition);
			}
			System.out.println("states covered = "+allStates.size()/((float)fsm.getStates().size()));
			System.out.println("transitions covered = "+allTransitions.size()/((float)fsm.getTransitions().size()));
			System.out.println("path length = "+cptPath.size());

			System.out.println(TimeUnit.MILLISECONDS.toSeconds(end-ini)+" seconds");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
