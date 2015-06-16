package test.com.usp.icmc.labes;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.RbacMutationUtils;

public class TestFsmTestingUtils {

	private static final FsmTestingUtils testingUtils = FsmTestingUtils.getInstance();
	private static final FsmUtils fsmUtils = FsmUtils.getInstance();
	private static final RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();

	@Ignore
	@Test
	public void stateCover() {
		List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
		policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK
		//		policies.add(TestExample.create_ProcureToStock());
		//		policies.add(TestExample.create_ProcureToStockV2());
		policies.add(TestExample.create_Masood2010Example1()); //XXX OK
		policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK
		//		policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1
		//		policies.add(TestExample.create_Masood2009P1());
		//		policies.add(TestExample.create_Masood2009P2());
		policies.add(TestExample.create_Masood2009P2v2());
		//		policies.add(TestExample.create_user11roles2());
		//		policies.add(TestExample.create_user5roles3());
		//		policies.add(TestExample.create_Mondal2009Example1());
		//		policies.add(TestExample.create_SecureBank());
		for (RbacPolicy rbacPol : policies) {

			try {
				RbacPolicy pol = rbacPol;

				long startTime, endTime;

				startTime = System.currentTimeMillis();
				FsmModel fsmGeneratedConcurrent = fsmUtils.rbac2FsmConcurrent(pol);
				endTime = System.currentTimeMillis();
				System.out.println("CONCUR: "+pol.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGeneratedConcurrent);

				startTime = System.currentTimeMillis();
				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(pol);
				endTime = System.currentTimeMillis();
				System.out.println("LINEAR: "+pol.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGenerated);

				assertEquals(fsmGenerated,fsmGeneratedConcurrent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testRbac2fsmConcLin() {
		List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
		policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK
		//		policies.add(TestExample.create_ProcureToStock());
		//		policies.add(TestExample.create_ProcureToStockV2());
		policies.add(TestExample.create_Masood2010Example1()); //XXX OK
		policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK
		//		policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1
		//		policies.add(TestExample.create_Masood2009P1());
		//		policies.add(TestExample.create_Masood2009P2());
//		policies.add(TestExample.create_Masood2009P2v2());
		//		policies.add(TestExample.create_user11roles2());
		//		policies.add(TestExample.create_user5roles3());
		//		policies.add(TestExample.create_Mondal2009Example1());
		//		policies.add(TestExample.create_SecureBank());
		
		Set<RbacPolicy> mutants = new HashSet<RbacPolicy>();		
		for (RbacPolicy rbacPol : policies) {
			File f = new File("policies/"+rbacPol.getName()+".rbac");
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
			mutants.add(rbacPol);
			for (MutantType mutantType : types) {
				mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));
			}
			
		}
		
		for (RbacPolicy mut : mutants) {
			try {

				long startTime, endTime;

				startTime = System.currentTimeMillis();
				FsmModel fsmGeneratedConcurrent = fsmUtils.rbac2FsmConcurrent(mut);
				endTime = System.currentTimeMillis();
				System.out.println("CONCUR: "+mut.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGeneratedConcurrent);

				startTime = System.currentTimeMillis();
				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(mut);
				endTime = System.currentTimeMillis();
				System.out.println("LINEAR: "+mut.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGenerated);

				assertEquals(fsmGenerated,fsmGeneratedConcurrent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	private static void sorting(FsmModel fsmGenerated) {
		fsmGenerated.getStates()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getTransitions()	.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getInputs()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getOutputs()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getStates().remove(fsmGenerated.getInitialState());
		fsmGenerated.getStates().add(0, fsmGenerated.getInitialState());
	}

	public static void main(String[] args) {
//		method01();
		List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
		policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK
		//		policies.add(TestExample.create_ProcureToStock());
		//		policies.add(TestExample.create_ProcureToStockV2());
//		policies.add(TestExample.create_Masood2010Example1()); //XXX OK
//		policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK
//				policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1
		//		policies.add(TestExample.create_Masood2009P1());
		//		policies.add(TestExample.create_Masood2009P2());
//		policies.add(TestExample.create_Masood2009P1v2());
//		policies.add(TestExample.create_Masood2009P2v2());
		//		policies.add(TestExample.create_user11roles2());
		//		policies.add(TestExample.create_user5roles3());
		//		policies.add(TestExample.create_Mondal2009Example1());
		//		policies.add(TestExample.create_SecureBank());
		
		Set<RbacPolicy> mutants = new HashSet<RbacPolicy>();		
		for (RbacPolicy rbacPol : policies) {
			File f = new File("policies/"+rbacPol.getName()+".rbac");
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
			mutants.add(rbacPol);
			for (MutantType mutantType : types) {
				mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));
			}
			
		}
		
		for (RbacPolicy mut : mutants) {
			try {

				File f = new File("policies/"+mut.getName()+".rbac");
//				long startTime, endTime;
//				System.out.print("CONCUR: "+mut.getName()+" : ");
//				startTime = System.currentTimeMillis();
//				FsmModel fsmGeneratedConcurrent = fsmUtils.rbac2FsmConcurrent(mut);
//				endTime = System.currentTimeMillis();
//				System.out.println(TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				TestRbacMutation.saveAllFormats(mut,  f.getParentFile());
//				sorting(fsmGeneratedConcurrent);

//				startTime = System.currentTimeMillis();
//				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(mut);
//				endTime = System.currentTimeMillis();
//				System.out.println("LINEAR: "+mut.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
//				sorting(fsmGenerated);
//
//				assertEquals(fsmGenerated,fsmGeneratedConcurrent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void method01() {
		List<RbacPolicy> policies= new ArrayList<RbacPolicy>();
		policies.add(TestExample.create_SeniorTraineeDoctor()); //XXX OK
		//		policies.add(TestExample.create_ProcureToStock());
		//				policies.add(TestExample.create_ProcureToStockV2());
		policies.add(TestExample.create_Masood2010Example1()); //XXX OK
		policies.add(TestExample.create_ExperiencePointsv2()); //XXX OK
		//		policies.add(TestExample.create_Masood2009Example1()); //XXX similar to Masood2010Example1
		//				policies.add(TestExample.create_Masood2009P1());
		//				policies.add(TestExample.create_Masood2009P2());
		policies.add(TestExample.create_Masood2009P2v2());
		//		policies.add(TestExample.create_user11roles2());
		//		policies.add(TestExample.create_user5roles3());
		//				policies.add(TestExample.create_Mondal2009Example1());
		//				policies.add(TestExample.create_SecureBank());
		for (RbacPolicy rbacPol : policies) {

			try {
				RbacPolicy pol = rbacPol;

				long startTime, endTime;

				startTime = System.currentTimeMillis();
				FsmModel fsmGeneratedConcurrent = fsmUtils.rbac2FsmConcurrent(pol);
				endTime = System.currentTimeMillis();
				System.out.println("CONCUR: "+pol.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGeneratedConcurrent);

				startTime = System.currentTimeMillis();
				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(pol);
				endTime = System.currentTimeMillis();
				System.out.println("LINEAR: "+pol.getName()+" : " + TimeUnit.MILLISECONDS.toMillis(endTime-startTime) + " ms");
				sorting(fsmGenerated);


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
