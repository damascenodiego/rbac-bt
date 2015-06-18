package test.com.usp.icmc.labes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class SoDTest {

	private RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private RbacUtils rbacUtils = RbacUtils.getInstance();
	private FsmUtils fsmUtils = FsmUtils.getInstance();

	private RbacPolicy policySD01() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testSD01");

		//create users
		u1 = new User("u1");
		//		u2 = new User("u2");

		//create role
		r1 = new Role("r1");
		r2 = new Role("r2");
		r3 = new Role("r3");

		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));
		//second add false
		assertFalse(rbacAdmin.addUser(rbac,u1));

		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));
		assertTrue(rbacAdmin.addRole(rbac,r2));
		assertTrue(rbacAdmin.addRole(rbac,r3));
		//		second add false
		assertFalse(rbacAdmin.addRole(rbac,r1));

		Role[] set = {r1,r2,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(set, 1));

//		Role[] dss = {r1,r2,r3};
//		rbac.getDSoDConstraint().add(new DSoDConstraint(dss, 2));

		return rbac;
	}

	private RbacPolicy policySD02() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testSD02");

		//create users
		u1 = new User("u1");
		//		u2 = new User("u2");

		//create role
		r1 = new Role("r1");
		r2 = new Role("r2");
		r3 = new Role("r3");

		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));
		//second add false
		assertFalse(rbacAdmin.addUser(rbac,u1));

		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));
		assertTrue(rbacAdmin.addRole(rbac,r2));
		assertTrue(rbacAdmin.addRole(rbac,r3));
		//		second add false
		assertFalse(rbacAdmin.addRole(rbac,r1));

		Role[] set = {r1,r2,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(set, 1));

		Role[] dss = {r1,r2,r3};
		rbac.getDSoDConstraint().add(new DSoDConstraint(dss, 2));

		return rbac;
	}

	private RbacPolicy policySD03() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;
	
		rbac = new RbacPolicy("testSD03");
	
		//create users
		u1 = new User("u1");
		//		u2 = new User("u2");
	
		//create role
		r1 = new Role("r1");
		r2 = new Role("r2");
		r3 = new Role("r3");
	
		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));
		//second add false
		assertFalse(rbacAdmin.addUser(rbac,u1));
	
		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));
		assertTrue(rbacAdmin.addRole(rbac,r2));
		assertTrue(rbacAdmin.addRole(rbac,r3));
		//		second add false
		assertFalse(rbacAdmin.addRole(rbac,r1));
	
		Role[] set1 = {r1,r2};
		Role[] set2 = {r2,r3};
		Role[] set3 = {r1,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(set1, 1));
		rbac.getSSoDConstraint().add(new SSoDConstraint(set2, 1));
		rbac.getSSoDConstraint().add(new SSoDConstraint(set3, 1));
	
	
		return rbac;
	}

	@Test
	public void testMutantsSSDDSDWithout(){
		try {
			RbacPolicy sd01 = policySD01();
			RbacPolicy sd02 = policySD02();
			RbacPolicy sd03 = policySD03();

			FsmModel fsm_sd01 = fsmUtils.rbac2FsmConcurrent(sd01);
			FsmModel fsm_sd02 = fsmUtils.rbac2FsmConcurrent(sd02);
			FsmModel fsm_sd03 = fsmUtils.rbac2FsmConcurrent(sd03);

			fsmUtils.sorting(fsm_sd01);
			fsmUtils.sorting(fsm_sd02);
			fsmUtils.sorting(fsm_sd03);
			
			assertTrue(fsm_sd01.equals(fsm_sd02));
			assertTrue(fsm_sd01.equals(fsm_sd03));
			assertTrue(fsm_sd02.equals(fsm_sd03));
		} catch (Exception e) {
			fail();
		}
	}
}
