//package test.com.usp.icmc.labes;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import com.usp.icmc.labes.fsm.FsmModel;
//import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
//import com.usp.icmc.labes.rbac.acut.RbacAcut;
//import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
//import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
//import com.usp.icmc.labes.rbac.model.Dr;
//import com.usp.icmc.labes.rbac.model.Du;
//import com.usp.icmc.labes.rbac.model.Permission;
//import com.usp.icmc.labes.rbac.model.RbacPolicy;
//import com.usp.icmc.labes.rbac.model.Role;
//import com.usp.icmc.labes.rbac.model.Sr;
//import com.usp.icmc.labes.rbac.model.Su;
//import com.usp.icmc.labes.rbac.model.User;
//import com.usp.icmc.labes.utils.FsmTestingUtils;
//import com.usp.icmc.labes.utils.FsmUtils;
//import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
//import com.usp.icmc.labes.utils.RbacUtils;
//
//public class MainTest {
//
//	public static void main(String[] args) {
//		MainTest mt = new MainTest();
//		mt.testFeasiblePolicies();
//	}
//	private RbacAdministrativeCommands rbacAdmin	= RbacAdministrativeCommands.getInstance();
//	private RbacSupportingSystemFunctions rbacSys 	= RbacSupportingSystemFunctions.getInstance();
//	private RbacUtils rbacUtils 					= RbacUtils.getInstance();
//	private FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();
//	private FsmUtils fsmUtils 						= FsmUtils.getInstance();
//
//
//	private PolicyUnderTestUtils putUtils 			= PolicyUnderTestUtils.getInstance();
//	RbacPolicy rbac;
//	RbacAcut acut;
//	User u1,u2;
//	Role r1;
//
//	Permission p1,p2;
//
//
//	public void saveAllFormats(RbacPolicy rbacPolicy) {
//		try {
//
//			File fRbacDir = new File("policies_example");
//			fRbacDir = new File(fRbacDir,rbacPolicy.getName());
//			fRbacDir.mkdirs();
//
//			File fRbac = new File(fRbacDir,rbacPolicy.getName()+".rbac");
//			File fFsm = new File(fRbacDir,rbacPolicy.getName()+".fsm");
//			File fDot = new File(fRbacDir,rbacPolicy.getName()+".dot");
//			File fKiss = new File(fRbacDir,rbacPolicy.getName()+".kiss");
//
//			if(!fRbac.exists()) {
//				rbacUtils.WriteRbacPolicyAsXML(rbacPolicy, fRbac);
//			}
//
//			FsmModel fsmConcurrentGenerated = null;
//			if(!fFsm.exists() && !fDot.exists() && !fKiss.exists()) {
//				fsmConcurrentGenerated = fsmUtils.rbac2FsmConcurrent(rbacPolicy);
//				fsmUtils.sorting(fsmConcurrentGenerated);
//				fsmUtils.WriteFsm(fsmConcurrentGenerated, fFsm);
//				if(!fDot.exists()) {
//					fsmUtils.WriteFsmAsDot(fsmConcurrentGenerated, fDot);
//				}
//				if(!fKiss.exists()) {
//					fsmUtils.WriteFsmAsKiss(fsmConcurrentGenerated, fKiss);
//				}
//			}
//			
//			
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	@Before
//	public void setupRbacPolicy() {
//		rbac = new RbacPolicy("rbacViaAdminCommand");
//
//		//create users
//		u1 = new User("u1");
//		u2 = new User("u2");
//
//		//create role
//		r1 = new Role("r1");
//
//		List<Su> su = new ArrayList<Su>();
//		List<Sr> sr = new ArrayList<Sr>();
//		List<Du> du = new ArrayList<Du>();
//		List<Dr> dr = new ArrayList<Dr>();
//
//		//create user cardinality constraints
//		su.add(new Su(u1, 1));
//		du.add(new Du(u1, 1));
//
//		//create role cardinality constraints
//		sr.add(new Sr(r1, 2));
//		dr.add(new Dr(r1, 1));
//
//		//create permissions
//		p1 = new Permission("p1");
//		p2 = new Permission("p2");
//
//		acut = new RbacAcut(rbac);
//		System.out.println(acut);
//
//		//add users to policy
//		//first add OK
//		assertTrue(rbacAdmin .addUser(rbac,u1));
//		//second add false
//		assertFalse(rbacAdmin.addUser(rbac,u1));
//
//		//add user constraints to policy
//		for (Su el : su) assertTrue(rbacAdmin .addSu(rbac,el));
//		for (Du el : du) assertTrue(rbacAdmin .addDu(rbac,el));
//
//		//add role constraints to policy
//		for (Sr el : sr) assertTrue(rbacAdmin .addSr(rbac,el));
//		for (Dr el : dr) assertTrue(rbacAdmin .addDr(rbac,el));
//
//		//first add OK
//		assertTrue(rbacAdmin.addUser(rbac,u2));
//		// second add false
//		assertFalse(rbacAdmin.addUser(rbac,u2));
//
//		//add role to policy
//		//first add ok
//		assertTrue(rbacAdmin.addRole(rbac,r1));
//		//		second add false
//		assertFalse(rbacAdmin.addRole(rbac,r1));
//
//		System.out.println(acut);
//
//
//		//add permissions
//		//first add ok
//		assertTrue(rbacAdmin.addPermission(rbac,p1));
//		assertTrue(rbacAdmin.addPermission(rbac,p2));
//		//second add false
//		assertFalse(rbacAdmin.addPermission(rbac,p1));
//		assertFalse(rbacAdmin.addPermission(rbac,p2));
//
//		//		for (int i = 2; i < 10; i++) {
//		//			//first add ok
//		//			assertTrue(rbacAdmin.addRole(rbac,new Role("r"+i,1,1)));
//		//		}
//
//		try {
//			rbacUtils.WriteRbacPolicyAsXML(rbac, new File("policies/Masood2010Example1.rbac"));
//			rbac = rbacUtils.loadRbacPolicyFromXML(new File("policies/Masood2010Example1.rbac"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	@Test
//	public void stateCoverAndTransitionCover() {
//
//		try {
//			RbacPolicy massod = putUtils.create_Masood2010Example1();
//			FsmModel fsmGenerated = fsmUtils.rbac2Fsm(massod);
//
//			File fsmFile = new File("test/Masood2010Example1.fsm");
//			File testSet = null;
//			fsmFile.getParentFile().mkdirs();
//
//			fsmUtils.WriteFsm(fsmGenerated, fsmFile);
//
//			FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);
//
//			assertEquals(fsmGenerated,fsm);
//
//			FsmTestSuite qSet = testingUtils.stateCoverSet(fsm);
//
//			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_qSet.test");
//			testingUtils.WriteFsmTestSuite(qSet, testSet);
//
//			assertEquals(qSet,testingUtils.loadFsmTestSuiteFromFile(testSet));
//			assertEquals(qSet.getTestCases().size(),fsm.getStates().size());
//
//			FsmTestSuite pSet = testingUtils.transitionCoverSet(fsm);
//
//			testSet = new File(fsmFile.getParentFile(),fsm.getName()+"_pSet.test");
//			testingUtils.WriteFsmTestSuite(pSet, testSet);
//
//			assertEquals(pSet,testingUtils.loadFsmTestSuiteFromFile(testSet));
//			assertEquals(pSet.getTestCases().size(),fsm.getStates().size()*fsm.getInputs().size());			
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	@Ignore
//	@Test
//	public void test() {
//
//		System.out.println(acut);
//
//		//create PR relationships
//		//first ok
//		assertTrue(rbacAdmin.grantPermission(rbac, p1,r1));
//		assertTrue(rbacAdmin.grantPermission(rbac, p2,r1));
//		//second false
//		assertFalse(rbacAdmin.grantPermission(rbac, p1,r1));
//		assertFalse(rbacAdmin.grantPermission(rbac, p2,r1));
//
//		System.out.println(acut);
//
//		//test activate non existing relationship
//		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
//
//		//create UR relationships
//		//first ok
//		assertTrue(rbacAdmin.assignUser(rbac, u1,r1));
//		assertTrue(rbacAdmin.assignUser(rbac, u2,r1));
//
//		//test activate existing relationship
//		assertTrue(rbacSys.addActiveRole(rbac, u1, r1));
//
//		//second false
//		assertFalse(rbacAdmin.assignUser(rbac, u1,r1));
//		assertFalse(rbacAdmin.assignUser(rbac, u2,r1));
//
//		//test activate non existing relationship
//		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
//
//		//test drop existing activation
//		assertTrue(rbacSys.dropActiveRole(rbac, u1, r1));
//
//		//test drop again activation
//		assertFalse(rbacSys.dropActiveRole(rbac, u1, r1));
//
//		//test activate existing relationship
//		assertTrue(rbacSys.addActiveRole(rbac, u1, r1));
//
//		//		u1.setStaticCardinality(0);
//		//		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
//		//		assertFalse(rbacAdmin.assignUser(rbac, u1, r1));
//		//		assertTrue(rbacAdmin.deassignUser(rbac, u1, r1));
//		//		assertFalse(rbacAdmin.deassignUser(rbac, u1, r1));
//		//		assertFalse(rbacAdmin.deassignUser(rbac, u1, r1));
//		//		assertFalse(rbacAdmin.assignUser(rbac, u1, r1));
//
//		System.out.println(acut);
//
//	}
//
//	@Test
//	public void testFeasiblePolicies() {
//
//		List<RbacPolicy> pols = putUtils.getFeasiblePoliciesUnderTest();
//
//		int count = pols.size();
//		for (int i = 0; i < pols.size(); i++) {
//			//		for (int i = pols.size()-1; i >= 0; i--) {
//			RbacPolicy rbacPolicy = pols.get(i);
//			saveAllFormats(rbacPolicy);
//			System.out.println(count--);
//		}
//	}
//
//	@Test
//	public void testRbac2Fsm() {
//
//		List<RbacPolicy> pols = putUtils.getSmallPoliciesUnderTest();
//
//
//		for (RbacPolicy rbacPolicy : pols) {
//			try {
//				FsmModel fsmGenerated = fsmUtils.rbac2Fsm(rbacPolicy);
//				FsmModel fsmConcurrentGenerated = fsmUtils.rbac2FsmConcurrent(rbacPolicy);
//
//				fsmUtils.sorting(fsmGenerated);
//				fsmUtils.sorting(fsmConcurrentGenerated);
//
//				assertEquals(fsmGenerated,fsmConcurrentGenerated);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//}
