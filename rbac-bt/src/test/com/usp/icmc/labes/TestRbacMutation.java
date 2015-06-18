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


public class TestRbacMutation {


	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();
	private static FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();

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

		try {
			List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
			//			policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK!!
			//			policies.add(TestExample.create_Masood2010Example1()); //XXX OK!!
			//			policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK!!
			//			policies.add(TestExample.create_Masood2009P2());
			//			policies.add(TestExample.create_Masood2009P2v2()); //XXX OK!!
			//			policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1

			//		policies.add(TestExample.create_ProcureToStock());
			//		policies.add(TestExample.create_ProcureToStockV2());
			
			//		policies.add(TestExample.create_Masood2009P1());
					policies.add(TestExample.create_Masood2009P1v2());

			//		policies.add(TestExample.create_user11roles2());
			
			//		policies.add(TestExample.create_user5roles3());
			
			//		policies.add(TestExample.create_Mondal2009Example1());
			
			//		policies.add(TestExample.create_SecureBank());

			for (RbacPolicy rbacPol : policies) {
				for (MutantType mutantType : types) mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));
			}

			for (RbacPolicy rbacPol : mutants) {
				for (MutantType mutantType : types) mutants2nd.addAll(rbacMut.generateMutants(rbacPol, mutantType));
			}
			List<RbacPolicy> mutants1st2nd = new ArrayList<RbacPolicy>();
			mutants1st2nd.addAll(policies);
			//			mutants1st2nd.addAll(mutants);
			//			mutants1st2nd.addAll(mutants2nd);	
			System.out.println("no. 1st order mutants: "+mutants.size());
			System.out.println("no. 2nd order mutants: "+mutants2nd.size());
			for (RbacPolicy rbacMutant : mutants1st2nd) {
				File f = new File("policies/"+rbacMutant.getName()+".rbac");
				saveAllFormats(rbacMutant,f.getParentFile());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

	protected static final void saveAllFormats(RbacPolicy rbacMutant, File outDir) throws Exception {
		File fMutant = null;
		if(rbacMutant instanceof RbacMutant){
			RbacPolicy pol = (RbacPolicy)((RbacMutant)rbacMutant).getPolicy();
			outDir = new File(outDir,pol.getName()+"/"+((RbacMutant)rbacMutant).getType().name()+"/");
			fMutant = new File(outDir,rbacMutant.getName()+".rbac");
		}else if(rbacMutant instanceof RbacPolicy){
			outDir = new File(outDir,rbacMutant.getName());
			fMutant = new File(outDir,rbacMutant.getName()+".rbac");
		}
		fMutant.getParentFile().mkdirs();

		RbacUtils.getInstance().WriteRbacPolicyAsXML(rbacMutant, fMutant);
		System.out.println(">>>>> WriteRbacPolicyAsXML finished"+ fMutant.getAbsoluteFile());

		FsmModel fsm = FsmUtils.getInstance().rbac2FsmConcurrent(rbacMutant);
		System.out.println(">>>>> rbac2fsm finished"+ fMutant.getName());

		File fsmFile = new File(outDir,rbacMutant.getName()+".fsm");
		System.out.println(">>>>> WriteFsm  started: "+ fsmFile.getAbsoluteFile());
		FsmUtils.getInstance().WriteFsm(fsm, fsmFile);

		File kissFile = new File(outDir,rbacMutant.getName()+".kiss");
		System.out.println(">>>>> WriteFsm  started: "+ kissFile.getAbsoluteFile());
		FsmUtils.getInstance().WriteFsmAsKiss(fsm, kissFile);


		//		FsmTestSuite set = testingUtils.stateCoverSet(fsm);
		//		System.out.println(">>>>> Q set generation: "+ fsmFile.getAbsoluteFile());
		//		File setFile = new File(fsmFile.getParentFile(),fsm.getName()+"_qSet.test");
		//		testingUtils.WriteFsmTestSuite(set, setFile);
		//
		//		set = testingUtils.transitionCoverSet(fsm);
		//		System.out.println(">>>>> P set generation: "+ fsmFile.getAbsoluteFile());
		//		setFile = new File(fsmFile.getParentFile(),fsm.getName()+"_pSet.test");
		//		testingUtils.WriteFsmTestSuite(set, setFile);

		File fsmFileFormat = new File(outDir,rbacMutant.getName()+".dot");
		System.out.println(">>>>> WriteFsmAsDot started: "+ fsmFileFormat.getAbsoluteFile());
		FsmUtils.getInstance().WriteFsmAsDot(fsm, fsmFileFormat);

		//		String runDot = "dot -Tpng "+fMutant.getParentFile().getAbsolutePath()+"/"+rbacMutant.getName()+".dot -o "+fMutant.getParentFile().getAbsolutePath()+"/"+rbacMutant.getName()+".png";
		//		Process p = Runtime.getRuntime().exec(runDot);
		//		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//		String s;
		//		while ((s = br.readLine()) != null) System.out.println("line: " + s);
		//		p.waitFor();
		//		System.out.println(runDot);
		//		System.out.println ("exit: " + p.exitValue());
		//		p.destroy();
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

