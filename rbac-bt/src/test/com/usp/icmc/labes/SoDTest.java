package test.com.usp.icmc.labes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class SoDTest {

	private RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private RbacUtils rbacUtils = RbacUtils.getInstance();
	private FsmUtils fsmUtils = FsmUtils.getInstance();

	@Test
	public void testSSD01() {
		RbacPolicy rbac = policySSD01();
		
		try {
			File rbacF = new File("policies/"+rbac.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbac, rbacF);
			FsmModel fsm = fsmUtils.rbac2Fsm(rbac);
			File dotF = new File("policies/"+rbac.getName()+".dot");
			fsmUtils.WriteFsmAsDot(fsm, dotF);

			String runDot = "dot -Tpng ./policies/"+rbac.getName()+".dot -o ./policies/"+rbac.getName()+".png";
			Process p = Runtime.getRuntime().exec(runDot);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = br.readLine()) != null) System.out.println("line: " + s);
			p.waitFor();
			System.out.println(runDot);
			System.out.println ("exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RbacPolicy policySSD01() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testSSD01");

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
		rbac.getSsodConstraint().add(new SSoDConstraint(set, 2));

		return rbac;
	}

	@Test
	public void testSSD02() {
		RbacPolicy rbac = policySSD02();
		try {
			File rbacF = new File("policies/"+rbac.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbac, rbacF);
			FsmModel fsm = fsmUtils.rbac2Fsm(rbac);
			File dotF = new File("policies/"+rbac.getName()+".dot");
			fsmUtils.WriteFsmAsDot(fsm, dotF);

			String runDot = "dot -Tpng ./policies/"+rbac.getName()+".dot -o ./policies/"+rbac.getName()+".png";
			Process p = Runtime.getRuntime().exec(runDot);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = br.readLine()) != null) System.out.println("line: " + s);
			p.waitFor();
			System.out.println(runDot);
			System.out.println ("exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RbacPolicy policySSD02() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testSSD02");

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


		//		Role[] set = {r1,r2,r3};
		//		rbac.getSsodConstraint().add(new SSoDConstraint(set, 2));

		return rbac;
	}

	@Test
	public void testDSD01() {
		RbacPolicy rbac = policyDSD01();

		try {
			File rbacF = new File("policies/"+rbac.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbac, rbacF);
			FsmModel fsm = fsmUtils.rbac2Fsm(rbac);
			File dotF = new File("policies/"+rbac.getName()+".dot");
			fsmUtils.WriteFsmAsDot(fsm, dotF);

			String runDot = "dot -Tpng ./policies/"+rbac.getName()+".dot -o ./policies/"+rbac.getName()+".png";
			Process p = Runtime.getRuntime().exec(runDot);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = br.readLine()) != null) System.out.println("line: " + s);
			p.waitFor();
			System.out.println(runDot);
			System.out.println ("exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RbacPolicy policyDSD01() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testDSD01");

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

		//			Role[] set = {r1,r2,r3};
		//			rbac.getSsodConstraint().add(new SSoDConstraint(set, 2));

		Role[] dss = {r1,r2,r3};
		rbac.getDsodConstraint().add(new DSoDConstraint(dss, 2));

		return rbac;
	}

	@Test
	public void testDSD02() {
		RbacPolicy rbac = policyDSD02();
		try {
			File rbacF = new File("policies/"+rbac.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbac, rbacF);
			FsmModel fsm = fsmUtils.rbac2Fsm(rbac);
			File dotF = new File("policies/"+rbac.getName()+".dot");
			fsmUtils.WriteFsmAsDot(fsm, dotF);

			String runDot = "dot -Tpng ./policies/"+rbac.getName()+".dot -o ./policies/"+rbac.getName()+".png";
			Process p = Runtime.getRuntime().exec(runDot);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = br.readLine()) != null) System.out.println("line: " + s);
			p.waitFor();
			System.out.println(runDot);
			System.out.println ("exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RbacPolicy policyDSD02() {
		RbacPolicy rbac;
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		rbac = new RbacPolicy("testDSD02");

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

		//			Role[] set = {r1,r2,r3};
		//			rbac.getSsodConstraint().add(new SSoDConstraint(set, 2));

		//		Role[] dss = {r1,r2,r3};
		//		rbac.getDsodConstraint().add(new DSoDConstraint(dss, 2));

		return rbac;
	}

	@Test
	public void testDSD03() {
		RbacPolicy rbac = policyDSD03();
		try {
			File rbacF = new File("policies/"+rbac.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbac, rbacF);
			FsmModel fsm = fsmUtils.rbac2Fsm(rbac);
			File dotF = new File("policies/"+rbac.getName()+".dot");
			fsmUtils.WriteFsmAsDot(fsm, dotF);

			String runDot = "dot -Tpng ./policies/"+rbac.getName()+".dot -o ./policies/"+rbac.getName()+".png";
			Process p = Runtime.getRuntime().exec(runDot);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s;
			while ((s = br.readLine()) != null) System.out.println("line: " + s);
			p.waitFor();
			System.out.println(runDot);
			System.out.println ("exit: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private RbacPolicy policyDSD03() {
		RbacAcut acut;
		User u1,u2;
		Role r1,r2,r3;
		Permission p1,p2;

		RbacPolicy rbac = new RbacPolicy("testDSD03");

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
		rbac.getSsodConstraint().add(new SSoDConstraint(set, 3));

		Role[] dss = {r1,r2,r3};
		rbac.getDsodConstraint().add(new DSoDConstraint(dss, 3));
		return rbac;
	}

	@Test
	public void testMutantsSSDDSDWithout(){
		RbacPolicy ssd02 = policySSD02();
		RbacPolicy dsd02 = policyDSD02();
		RbacPolicy dsd03 = policyDSD03();
		
		FsmModel fsm_ssd02 = fsmUtils.rbac2Fsm(ssd02);
		FsmModel fsm_dsd02 = fsmUtils.rbac2Fsm(dsd02);
		FsmModel fsm_dsd03 = fsmUtils.rbac2Fsm(dsd03);
		
		assertTrue(fsm_ssd02.equals(fsm_dsd02));
		assertTrue(fsm_ssd02.equals(fsm_dsd03));
		assertTrue(fsm_dsd02.equals(fsm_dsd03));
	}
}
