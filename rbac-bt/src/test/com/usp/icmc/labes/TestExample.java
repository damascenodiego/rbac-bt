package test.com.usp.icmc.labes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.RoleConstraint;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserConstraint;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

public class TestExample {


	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();


	public static void main(String[] args) {
		try {
			RbacPolicy rbacPol = create_Masood2009P1();
			File f = new File("policies/"+rbacPol.getName()+".rbac");
			RbacUtils.getInstance().WriteRbacPolicyAsXML(rbacPol, f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static RbacPolicy create_SeniorTraineeDoctor(){
		RbacPolicy rbac = new RbacPolicy("SeniorTraineeDoctor");

		//create users
		User u1 = new User("Bob");
		User u2 = new User("Alice");
		//add to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);

		//create user cardinality constraints
		UserConstraint u1Card = new UserConstraint(u1, 2,2);
		UserConstraint u2Card = new UserConstraint(u2, 1,1);
		//add to policy
		rbac.getUserCard().add(u1Card);
		rbac.getUserCard().add(u2Card);

		//create role
		Role r1 = new Role("SeniorDoctor");
		Role r2 = new Role("TraineeDoctor");
		//add to policy
		rbac.getRole().add(r1);
		rbac.getRole().add(r2);

		//create role cardinality constraints
		RoleConstraint r1Card = new RoleConstraint(r1, 1,1);
		RoleConstraint r2Card = new RoleConstraint(r2, 2,2);
		//add to policy
		rbac.getRoleCard().add(r1Card);
		rbac.getRoleCard().add(r2Card);

		Role [] sodSet = {r1,r2};
		SSoDConstraint sod = new SSoDConstraint(sodSet, 1);
		//add to policy
		rbac.getSsodConstraint().add(sod);

		return rbac;
	}
	private static RbacPolicy create_Masood2010Example1(){
		RbacPolicy rbac = new RbacPolicy("Masood2010Example1");

		//create users
		User u1 = new User("u1");
		User u2 = new User("u2");
		//add to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);

		//create role
		Role r1 = new Role("r1");
		//add to policy
		rbac.getRole().add(r1);

		//create permission
		Permission p1 = new Permission("p1");
		Permission p2 = new Permission("p2");
		//add to policy
		rbac.getPermission().add(p1);
		rbac.getPermission().add(p2);

		//create user cardinality constraints
		UserConstraint u1Card = new UserConstraint(u1, 1,1);
		UserConstraint u2Card = new UserConstraint(u2, 1,1);
		//add to policy
		rbac.getUserCard().add(u1Card);
		rbac.getUserCard().add(u2Card);


		//create role cardinality constraints
		RoleConstraint r1Card = new RoleConstraint(r1, 2,1);
		//add to policy
		rbac.getRoleCard().add(r1Card);

		return rbac;
	}
	private static RbacPolicy create_ProcureToStock(){
		RbacPolicy rbac = new RbacPolicy("ProcureToStock");

		//create users
		User u1 = new User("Alice");
		User u2 = new User("Bob");
		User u3 = new User("Carol");
		User u4 = new User("Employee Name");
		//add to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);
		rbac.getUser().add(u3);
		rbac.getUser().add(u4);

		//create role
		Role r1 = new Role("Role1");
		Role r2 = new Role("Role2");
		Role r3 = new Role("Role3");
		Role r4 = new Role("Role4");
		Role r5 = new Role("Role5");
		//add to policy
		rbac.getRole().add(r1);
		rbac.getRole().add(r2);
		rbac.getRole().add(r3);
		rbac.getRole().add(r4);
		rbac.getRole().add(r5);

		//create permission
		Permission p1 = new Permission("CreatePR");
		Permission p2 = new Permission("ConfirmPR");
		Permission p3 = new Permission("CancelPR");
		Permission p4 = new Permission("!ReqPrice");
		Permission p5 = new Permission("!ReqExpert");
		Permission p6 = new Permission("?BestPrice");
		Permission p7 = new Permission("?BestExpert");
		Permission p8 = new Permission("?NoSupplier");
		Permission p9 = new Permission("SendPO");
		Permission p10 = new Permission("RecInvoice");
		Permission p11 = new Permission("RecCreditNote");
		Permission p12 = new Permission("NoCreditNoteRec");
		Permission p13 = new Permission("PaymentProcess");
		Permission p14 = new Permission("RecPaymentConf");
		Permission p15 = new Permission("BlockGoods");

		//add to policy
		rbac.getPermission().add(p1);
		rbac.getPermission().add(p2);
		rbac.getPermission().add(p3);
		rbac.getPermission().add(p4);
		rbac.getPermission().add(p5);
		rbac.getPermission().add(p6);
		rbac.getPermission().add(p7);
		rbac.getPermission().add(p8);
		rbac.getPermission().add(p9);
		rbac.getPermission().add(p10);
		rbac.getPermission().add(p11);
		rbac.getPermission().add(p12);
		rbac.getPermission().add(p13);
		rbac.getPermission().add(p14);
		rbac.getPermission().add(p15);

		return rbac;
	}
	private static RbacPolicy create_ExperiencePoints(){
		RbacPolicy rbac = new RbacPolicy("ExperiencePoints");

		//create users
		//		User u1 = new User("u1");
		//		User u2 = new User("u2");
		//		//add to policy
		//		rbac.getUser().add(u1);
		//		rbac.getUser().add(u2);

		//create role
		Role admin = new Role("Admin");
		Role bronze = new Role("Bronze");
		Role silver = new Role("Silver");
		Role gold = new Role("Gold");
		//add to policy
		rbac.getRole().add(admin);
		rbac.getRole().add(bronze);
		rbac.getRole().add(silver);
		rbac.getRole().add(gold);

		//create permission
		Permission p1 = new Permission("ReplyPost");
		Permission p2 = new Permission("NewPost");
		Permission p3 = new Permission("BeReplied");
		Permission p4 = new Permission("Download");
		//add to policy
		rbac.getPermission().add(p1);
		rbac.getPermission().add(p2);
		rbac.getPermission().add(p3);
		rbac.getPermission().add(p4);

		return rbac;
	}
	private static RbacPolicy create_Masood2009Example1(){
		RbacPolicy rbac = new RbacPolicy("Masood2009Example1");

		//create users
		User u1 = new User("John");
		User u2 = new User("Mary");

		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));

		//first add OK
		assertTrue(rbacAdmin.addUser(rbac,u2));

		//create user cardinality constraints
		UserConstraint u1Card = new UserConstraint(u1, 1,1);
		UserConstraint u2Card = new UserConstraint(u2, 1,1);

		//add user constraints to policy
		assertTrue(rbacAdmin .addUserConstraint(rbac,u1Card));
		assertTrue(rbacAdmin .addUserConstraint(rbac,u2Card));

		//create role
		Role r1 = new Role("Customer");

		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));

		//create role cardinality constraints
		RoleConstraint r1Card = new RoleConstraint(r1, 2,1);

		//add role constraints to policy
		assertTrue(rbacAdmin .addRoleConstraint(rbac,r1Card));


		//create permissions
		Permission p1 = new Permission("Deposit");
		Permission p2= new Permission("Withdraw");
		//add permissions
		//first add ok
		assertTrue(rbacAdmin.addPermission(rbac,p1));
		assertTrue(rbacAdmin.addPermission(rbac,p2));

		//create UR relationships
		//first ok
		assertTrue(rbacAdmin.assignUser(rbac, u1,r1));
		assertTrue(rbacAdmin.assignUser(rbac, u2,r1));

		//create PR relationships
		//first ok
		assertTrue(rbacAdmin.grantPermission(rbac, p1,r1));
		assertTrue(rbacAdmin.grantPermission(rbac, p2,r1));

		return rbac;
	}

	private static RbacPolicy create_Masood2009P1(){
		RbacPolicy rbac = new RbacPolicy("Masood2009P1");

		//create users
		User u1 = new User("U1");
		User u2 = new User("U2");
		User u3 = new User("U3");
		User u4 = new User("U4");
		User u5 = new User("U5");

		//add users to policy
		rbacAdmin .addUser(rbac,u1);
		rbacAdmin .addUser(rbac,u2);
		rbacAdmin .addUser(rbac,u3);
		rbacAdmin .addUser(rbac,u4);
		rbacAdmin .addUser(rbac,u5);

		
		rbacAdmin .addUserConstraint(rbac,new UserConstraint(u1, -1,2));
		rbacAdmin .addUserConstraint(rbac,new UserConstraint(u2, -1,2));
		rbacAdmin .addUserConstraint(rbac,new UserConstraint(u3, -1,2));
		rbacAdmin .addUserConstraint(rbac,new UserConstraint(u4, -1,2));
		rbacAdmin .addUserConstraint(rbac,new UserConstraint(u5, -1,1));

		
		//create role
		Role r1 = new Role("R1");
		Role r2 = new Role("R2");
		Role r3 = new Role("R3");
		Role r4 = new Role("R4");

		//add role to policy
		rbacAdmin.addRole(rbac,r1);
		rbacAdmin.addRole(rbac,r2);
		rbacAdmin.addRole(rbac,r3);
		rbacAdmin.addRole(rbac,r4);

		//add role constraints to policy
		rbacAdmin .addRoleConstraint(rbac,new RoleConstraint(r1, -1,3));
		rbacAdmin .addRoleConstraint(rbac,new RoleConstraint(r2, -1,1));
		rbacAdmin .addRoleConstraint(rbac,new RoleConstraint(r3, -1,3));
		rbacAdmin .addRoleConstraint(rbac,new RoleConstraint(r4, -1,2));

		Role[] ssodSet = {r1,r2};
		rbac.getSsodConstraint().add(new SSoDConstraint(ssodSet, 1));
		Role[] dsodSet = {r2,r3};
		rbac.getSsodConstraint().add(new SSoDConstraint(dsodSet, 1));
		
		return rbac;
	}
}
