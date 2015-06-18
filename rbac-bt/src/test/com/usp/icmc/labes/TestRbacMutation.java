package test.com.usp.icmc.labes;

import java.io.File;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
import com.usp.icmc.labes.utils.RbacUtils;


public class TestRbacMutation {

	private static FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();

	public static void main(String[] args) {

		try {
			
			List<RbacPolicy> mutants1st2nd = PolicyUnderTestUtils.getInstance().getAllPoliciesUnderTest();
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

