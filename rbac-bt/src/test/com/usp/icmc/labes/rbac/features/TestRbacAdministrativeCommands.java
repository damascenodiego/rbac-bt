package test.com.usp.icmc.labes.rbac.features;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class TestRbacAdministrativeCommands {

	private RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private RbacUtils rbacUtils = RbacUtils.getInstance();
	
	
	@Test
	public void test() {

		RbacPolicy policyModel = rbacViaModelMethods();
		RbacPolicy policyAdmin = rbacViaAdminCommand();

		for (User el : policyModel.getUser()) {
			assertTrue(rbacUtils.userExists(policyAdmin, el));
			
		}
		for (Role el : policyModel.getRole()) {
			assertTrue(rbacUtils.roleExists(policyAdmin, el));
		}
		for (Permission el : policyModel.getPermission()) {
			assertTrue(rbacUtils.permissionExists(policyAdmin, el));
		}
		for (UserRoleAssignment el : policyModel.getUserRoleAssignment()) {
			assertTrue(rbacUtils.userRoleAssignmentExists(policyAdmin, el.getUser(),el.getRole()));
		}

		for (PermissionRoleAssignment el : policyModel.getPermissionRoleAssignment()) {
			assertTrue(rbacUtils.permissionRoleAssignmentExists(policyAdmin, el.getPermission(),el.getRole()));
		}

		assertEquals(policyModel, policyAdmin);
		
		File policyModelFile = new File("policyModel.rbac");
		File policyAdminFile = new File("policyAdmin.rbac");
		try {
			rbacUtils.WriteRbacPolicyAsXML(policyModel, policyModelFile);
			rbacUtils.WriteRbacPolicyAsXML(policyAdmin, policyAdminFile);
			
			policyAdmin = null;
			policyModel = null;
			
			policyModel= rbacUtils.LoadRbacPolicyFromXML(policyModelFile);
			policyAdmin = rbacUtils.LoadRbacPolicyFromXML(policyAdminFile);
			
			assertEquals(policyModel, policyAdmin);
			
//			policyAdminFile.delete();
//			policyModelFile.delete();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(policyModel, policyAdmin);
		
	}


	private RbacPolicy rbacViaModelMethods() {
		RbacPolicy rbac = new RbacPolicy("rbacViaModelMethods");

		//create users
		User u1 = new User("u1",1,1);
		User u2 = new User("u2",1,1);

		//create role
		Role r1 = new Role("r1",2,1);

		//create permissions
		Permission p1 = new Permission("p1");
		Permission p2= new Permission("p2");
		
		//add users to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);

		//add role to policy
		rbac.getRole().add(r1);
		//add permissions
		rbac.getPermission().add(p1);
		rbac.getPermission().add(p2);

		//create UR relationships
		UserRoleAssignment u1r1 = new UserRoleAssignment(u1,r1);
		UserRoleAssignment u2r1 = new UserRoleAssignment(u2,r1);
		//create PR relationships
		PermissionRoleAssignment r1p1 = new PermissionRoleAssignment(p1,r1);
		PermissionRoleAssignment r1p2 = new PermissionRoleAssignment(p2,r1);

		//add UR to policy
		rbac.getUserRoleAssignment().add(u1r1);
		rbac.getUserRoleAssignment().add(u2r1);

		// activate r1
		UserRoleActivation ua = new UserRoleActivation(u1, r1);
		rbac. getUserRoleActivation().add(ua);
		
		//add PR to policy
		rbac.getPermissionRoleAssignment().add(r1p1);
		rbac.getPermissionRoleAssignment().add(r1p2);

		return rbac;
	}

	private RbacPolicy rbacViaAdminCommand(){
		RbacPolicy rbac = new RbacPolicy("rbacViaAdminCommand");

		//create users
		User u1 = new User("u1",1,1);
		User u2 = new User("u2",1,1);

		//create role
		Role r1 = new Role("r1",2,1);

		//create permissions
		Permission p1 = new Permission("p1");
		Permission p2= new Permission("p2");
		
		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));
		//second add false
		assertFalse(rbacAdmin.addUser(rbac,u1));

		//first add OK
		assertTrue(rbacAdmin.addUser(rbac,u2));
		// second add false
		assertFalse(rbacAdmin.addUser(rbac,u2));

		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));
		//		second add false
		assertFalse(rbacAdmin.addRole(rbac,r1));


		//add permissions
		//first add ok
		assertTrue(rbacAdmin.addPermission(rbac,p1));
		assertTrue(rbacAdmin.addPermission(rbac,p2));
		//second add false
		assertFalse(rbacAdmin.addPermission(rbac,p1));
		assertFalse(rbacAdmin.addPermission(rbac,p2));

		//test activate non existing relationship
		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
		
		//create UR relationships
		//first ok
		assertTrue(rbacAdmin.assignUser(rbac, u1,r1));
		assertTrue(rbacAdmin.assignUser(rbac, u2,r1));
		
		//test activate existing relationship
		assertTrue(rbacSys.addActiveRole(rbac, u1, r1));
		
		//second false
		assertFalse(rbacAdmin.assignUser(rbac, u1,r1));
		assertFalse(rbacAdmin.assignUser(rbac, u2,r1));

		//test activate non existing relationship
		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
		
		//test drop existing activation
		assertTrue(rbacSys.dropActiveRole(rbac, u1, r1));
		
		//test drop again activation
		assertFalse(rbacSys.dropActiveRole(rbac, u1, r1));
		
		//test activate existing relationship
		assertTrue(rbacSys.addActiveRole(rbac, u1, r1));
		
		//create PR relationships
		//first ok
		assertTrue(rbacAdmin.grantPermission(rbac, p1,r1));
		assertTrue(rbacAdmin.grantPermission(rbac, p2,r1));
		//second false
		assertFalse(rbacAdmin.grantPermission(rbac, p1,r1));
		assertFalse(rbacAdmin.grantPermission(rbac, p2,r1));

		return rbac;
	}

}
