package test.com.usp.icmc.labes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.testing.FsmMutationTestRun;
import com.usp.icmc.labes.fsm.testing.FsmSUT;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.RbacTuple;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
import com.usp.icmc.labes.utils.RbacMutationUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class TestMutationTesting {


	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static FsmTestingUtils fsmTestingUtils = FsmTestingUtils.getInstance();
	private static FsmUtils fsmUtils = FsmUtils.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();
	private static PolicyUnderTestUtils putUtils = PolicyUnderTestUtils.getInstance();

	public static void main(String[] args) {
		MutantType types[] = {
				MutantType.UR_REPLACE_U, 
				MutantType.UR_REPLACE_R, 
				MutantType.UR_REPLACE_UR,
				MutantType.Su_INCREMENT, 
				MutantType.Su_DECREMENT,
				MutantType.Du_INCREMENT, 
				MutantType.Du_DECREMENT,
				MutantType.Sr_INCREMENT, 
				MutantType.Sr_DECREMENT,
				MutantType.Dr_INCREMENT, 
				MutantType.Dr_DECREMENT,
				MutantType.SSoD_REPLACE,
				MutantType.DSoD_REPLACE,
				MutantType.Ss_INCREMENT, 
				MutantType.Ss_DECREMENT,
				MutantType.Ds_INCREMENT, 
				MutantType.Ds_DECREMENT,
		};
		List<RbacPolicy> mutants = new ArrayList<RbacPolicy>();
		List<RbacPolicy> mutants2nd = new ArrayList<RbacPolicy>();

		List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
		//				RbacTuple rbacPol = putUtils.create_SeniorTraineeDoctor()); 		//XXX OK!! 1st and 2nd order mutants
//		RbacPolicy rbacPol = putUtils.create_Masood2010Example1(); 			//XXX OK!! 1st and 2nd order mutants
		RbacPolicy rbacPol = putUtils.create_ExperiencePointsv2(); 			//XXX OK!! 1st and 2nd order mutants
		//		//////RbacTuple rbacPol = putUtils.create_Masood2009P2());
		//		RbacTuple rbacPol = putUtils.create_Masood2009P2v2()); 				//XXX OK!! 1st order mutants  //TODO executar durante madrugada
		//////RbacTuple rbacPol = putUtils.create_Masood2009Example1()); //XXX similar to Masood2010Example1
		//////RbacTuple rbacPol = putUtils.create_user5roles3());
		//////RbacTuple rbacPol = putUtils.create_userXrolesY());
		//////RbacTuple rbacPol = putUtils.create_user11roles2());
		//		RbacTuple rbacPol = putUtils.create_user11roles2v2()); 				//XXX OK!! 1st and 2nd order mutants
		//		//////RbacTuple rbacPol = putUtils.create_Masood2009P1());
		//		RbacTuple rbacPol = putUtils.create_Masood2009P1v2()); 				//XXX OK!! 1st and 2nd order mutants
		//		//////RbacTuple rbacPol = putUtils.create_ProcureToStock());
		//		RbacTuple rbacPol = putUtils.create_ProcureToStockV2());			//XXX OK!! 1st order mutants


		////////RbacTuple rbacPol = putUtils.create_Mondal2009Example1()); 			// TODO implement hierarchies
		////////RbacTuple rbacPol = putUtils.create_SecureBank());					// TODO implement hierarchies

		FsmModel fsm = fsmUtils.rbac2FsmConcurrent(rbacPol);
		List<RbacRequest> input = new ArrayList<RbacRequest>();
		for (Role rol: rbacPol.getRole()) {
			for (User usr: rbacPol.getUser()) {
				input.add(new RbacRequestAssignUR(usr, rol));
				input.add(new RbacRequestDeassignUR(usr, rol));
				input.add(new RbacRequestActivateUR(usr, rol));
				input.add(new RbacRequestDeactivateUR(usr, rol));
			}
		}
		boolean[][] statesOut =  new boolean[fsm.getStates().size()][];
		for (FsmState s : fsm.getStates()) {
			boolean[] out = new boolean[input.size()];
			for (RbacRequest rbacRequest : input) {
				RbacAcut acut = new RbacAcut(rbacPol);
				RbacState currState = new RbacState(rbacPol, s);
				acut.reset(currState);
				out[input.indexOf(rbacRequest)] = acut.request(rbacRequest); 
			}
			statesOut[fsm.getStates().indexOf(s)] = out;
		}
		for (boolean[] b : statesOut) {
			System.out.print("[");
			for (boolean c : b) {
				System.out.print(c+" ");
			}
			System.out.println("]");
		}
//		FsmTestSuite testSuite = fsmTestingUtils.transitionTour(fsm);
//		
//		for (MutantType mutantType : types) 
//			mutants.addAll(rbacMut.generateMutants(rbacPol , mutantType));
//		
//		FsmMutationTestRun run = new FsmMutationTestRun(fsm, testSuite);
//		
//		for (RbacPolicy rbacPolicy : mutants) {
//			FsmSUT sut = new FsmSUT(fsmUtils.rbac2FsmConcurrent(rbacPolicy));
//			run.addSut(sut);
//		}
//		
//		run.testInputWithOutput();
//		
//		System.out.println(
//				run.getAlivePolicies().size()
//				/
//				((float)run.getKilledPolicies().size()+run.getAlivePolicies().size())
//				);
//
//		Set<FsmModel> set = new HashSet<FsmModel>();
//		for (FsmSUT sut : run.getAlivePolicies()) {
//			set.add(sut.getSut());	
//		}
//		
//		System.out.println(set.size());
		
	}


}
