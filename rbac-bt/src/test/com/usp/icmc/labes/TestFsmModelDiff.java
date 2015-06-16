package test.com.usp.icmc.labes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.RbacMutationUtils;
import com.usp.icmc.labes.utils.RbacUtils;


public class TestFsmModelDiff {


	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();
	private static FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();
	private static FsmUtils fsmUtils = FsmUtils.getInstance();

	public static void main(String[] args) {

		try {
			List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
			policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK
			//		policies.add(TestExample.create_ProcureToStock());
			//		policies.add(TestExample.create_ProcureToStockV2());
//			policies.add(TestExample.create_Masood2010Example1()); //XXX OK
//			policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK
//					policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1
			//		policies.add(TestExample.create_Masood2009P1());
			//		policies.add(TestExample.create_Masood2009P2());
//			policies.add(TestExample.create_Masood2009P1v2());
//			policies.add(TestExample.create_Masood2009P2v2());
			//		policies.add(TestExample.create_user11roles2());
			//		policies.add(TestExample.create_user5roles3());
			//		policies.add(TestExample.create_Mondal2009Example1());
			//		policies.add(TestExample.create_SecureBank());
			

			for (RbacPolicy rbacPol : policies) {
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
						MutantType.Ds_DECREMENT,};
				Set<RbacPolicy> mutants = new HashSet<RbacPolicy>();
				mutants.add(rbacPol);
				for (MutantType mutantType : types) {
					mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));
				}
				
				FsmModel fsm = FsmUtils.getInstance().rbac2FsmConcurrent(rbacPol);
				for (RbacPolicy rbacMutant : mutants) {
					FsmModel fsmMut = FsmUtils.getInstance().rbac2FsmConcurrent(rbacMutant);
					File fDiff = new File("diff/"+rbacMutant.getName()+".diff.dot");
					fDiff.getParentFile().mkdirs();
					fsmUtils.fsmDiff(fsm, fsmMut, fDiff);
					
					String runDot = "dot -Tpng "+fDiff.getAbsolutePath()+" -o "+fDiff.getAbsolutePath()+".png";
					Process p = Runtime.getRuntime().exec(runDot);
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String s;
					while ((s = br.readLine()) != null) System.out.println("line: " + s);
					p.waitFor();
					System.out.println(runDot);
					System.out.println ("exit: " + p.exitValue());
					p.destroy();
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}



// remove later
//		ActivationHierarchy ah = new ActivationHierarchy(r1, r2);
//		InheritanceHierarchy ih = new InheritanceHierarchy(r3, r4);
//		rbac.getActivationHierarchy().add(ah);
//		rbac.getInheritanceHierarchy().add(ih);
//		rbacAdmin .addSr(rbac,new Sr(r4, 1));
//		rbacAdmin .addSu(rbac,new Su(u4, 1));
//
//		rbacAdmin .assignUser(rbac, u1, r1);
//		Permission p1 = new Permission("XXX");
//		rbacAdmin .addPermission(rbac, p1);
//		rbacSys. addActiveRole(rbac, u1,r1);
//		rbacAdmin .grantPermission(rbac, p1, r1);

