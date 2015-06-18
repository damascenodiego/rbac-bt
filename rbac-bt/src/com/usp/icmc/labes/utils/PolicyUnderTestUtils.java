package com.usp.icmc.labes.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;

public class PolicyUnderTestUtils {

	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static FsmTestingUtils fsmTestingUtils = FsmTestingUtils.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();


	private static PolicyUnderTestUtils instance;

	private PolicyUnderTestUtils() {
	}

	public static PolicyUnderTestUtils getInstance() {
		if(instance == null){
			instance = new PolicyUnderTestUtils();
		}
		return instance;
	}

	public RbacPolicy create_SeniorTraineeDoctor(){
		RbacPolicy rbac = new RbacPolicy("SeniorTraineeDoctor");

		//create users
		User u1 = new User("Bob");
		User u2 = new User("Alice");
		//add to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);

		//create user cardinality constraints
		Su su1 = new Su(u1, 2);
		Du du1 = new Du(u1, 2);
		Su su2 = new Su(u2, 1);
		Du du2 = new Du(u2, 1);

		//add to policy
		rbac.getSu().add(su1);
		rbac.getSu().add(su2);
		rbac.getDu().add(du1);
		rbac.getDu().add(du2);

		//create role
		Role r1 = new Role("SeniorDoctor");
		Role r2 = new Role("TraineeDoctor");
		//add to policy
		rbac.getRole().add(r1);
		rbac.getRole().add(r2);

		//create role cardinality constraints
		Sr sr1 = new Sr(r1, 1);
		Dr dr1 = new Dr(r1, 1);
		Sr sr2 = new Sr(r2, 2);
		Dr dr2 = new Dr(r2, 2);

		//add to policy
		rbac.getSr().add(sr1);
		rbac.getDr().add(dr1);
		rbac.getSr().add(sr2);
		rbac.getDr().add(dr2);

		Role [] sodSet = {r1,r2};
		SSoDConstraint sod = new SSoDConstraint(sodSet, 1);
		//add to policy
		rbac.getSSoDConstraint().add(sod);

		return rbac;
	}

	public RbacPolicy create_ProcureToStock(){
		RbacPolicy rbac = new RbacPolicy("ProcureToStock");

		//create users
		User alice = new User("Alice");
		User bob = new User("Bob");
		User carol = new User("Carol");
		User employee = new User("Employee Name");
		//add to policy
		rbac.getUser().add(alice);
		rbac.getUser().add(carol);
		rbac.getUser().add(bob);
		rbac.getUser().add(employee);

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

		rbacAdmin.assignUser(rbac, alice, r1);
		rbacAdmin.assignUser(rbac, alice, r2);
		rbacAdmin.assignUser(rbac, alice, r3);
		rbacAdmin.assignUser(rbac, alice, r4);
		rbacAdmin.assignUser(rbac, alice, r5);

		rbacAdmin.assignUser(rbac, bob, r1);
		rbacAdmin.assignUser(rbac, bob, r2);
		rbacAdmin.assignUser(rbac, bob, r3);
		rbacAdmin.assignUser(rbac, bob, r5);

		rbacAdmin.assignUser(rbac, carol, r4);
		rbacAdmin.assignUser(rbac, carol, r5);

		rbacAdmin.assignUser(rbac, employee, r1);
		rbacAdmin.assignUser(rbac, employee, r4);

		rbacAdmin.grantPermission(rbac, p1, r1);

		rbacAdmin.grantPermission(rbac, p2, r2);
		rbacAdmin.grantPermission(rbac, p3, r2);

		rbacAdmin.grantPermission(rbac, p4, r3);
		rbacAdmin.grantPermission(rbac, p5, r3);
		rbacAdmin.grantPermission(rbac, p6, r3);
		rbacAdmin.grantPermission(rbac, p7, r3);
		rbacAdmin.grantPermission(rbac, p8, r3);
		rbacAdmin.grantPermission(rbac, p9, r3);

		rbacAdmin.grantPermission(rbac, p10, r4);
		rbacAdmin.grantPermission(rbac, p11, r4);
		rbacAdmin.grantPermission(rbac, p12, r4);

		rbacAdmin.grantPermission(rbac, p13, r5);
		rbacAdmin.grantPermission(rbac, p14, r5);
		rbacAdmin.grantPermission(rbac, p15, r5);

		return rbac;
	}

	public RbacPolicy create_Masood2010Example1(){
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
		Su su1 = new Su(u1, 1);
		Du du1 = new Du(u1, 1);
		Su su2 = new Su(u2, 1);
		Du du2 = new Du(u2, 1);
		//add to policy
		rbac.getSu().add(su1);
		rbac.getSu().add(su2);
		rbac.getDu().add(du1);
		rbac.getDu().add(du2);

		//create role cardinality constraints
		Sr sr1 = new Sr(r1, 2);
		Dr dr1 = new Dr(r1, 1);
		//add to policy
		rbac.getSr().add(sr1);
		rbac.getDr().add(dr1);


		rbacAdmin.assignUser(rbac, u1,r1);
		rbacAdmin.assignUser(rbac, u2,r1);

		rbacAdmin.grantPermission(rbac, p1,r1);
		rbacAdmin.grantPermission(rbac, p2,r1);

		return rbac;
	}

	public RbacPolicy create_Masood2009Example1(){
		RbacPolicy rbac = new RbacPolicy("Masood2009Example1");

		//create users
		User u1 = new User("John");
		User u2 = new User("Mary");

		//add users to policy
		rbacAdmin .addUser(rbac,u1);
		rbacAdmin .addUser(rbac,u2);


		rbacAdmin .addSu(rbac,new Su(u1, 1));
		rbacAdmin .addDu(rbac,new Du(u1, 1));

		rbacAdmin .addSu(rbac,new Su(u2, 1));
		rbacAdmin .addDu(rbac,new Du(u2, 1));

		//create role
		Role r1 = new Role("Customer");

		//add role to policy
		rbacAdmin.addRole(rbac,r1);

		//add role constraints to policy
		rbacAdmin .addSr(rbac,new Sr(r1, 2));
		rbacAdmin .addDr(rbac,new Dr(r1, 1));

		rbacAdmin.assignUser(rbac, u1, r1);
		rbacAdmin.assignUser(rbac, u2, r1);

		Permission p1 = new Permission("Deposit");
		Permission p2 = new Permission("Withdraw");
		rbacAdmin .addPermission(rbac, p1);
		rbacAdmin .addPermission(rbac, p2);

		rbacAdmin .grantPermission(rbac, p1, r1);
		rbacAdmin .grantPermission(rbac, p2, r1);

		return rbac;
	}

	public RbacPolicy create_Masood2009P1(){
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


		rbacAdmin .addDu(rbac,new Du(u1, 2));
		rbacAdmin .addDu(rbac,new Du(u2, 2));
		rbacAdmin .addDu(rbac,new Du(u3, 2));
		rbacAdmin .addDu(rbac,new Du(u4, 2));
		rbacAdmin .addDu(rbac,new Du(u5, 1));

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
		rbacAdmin .addDr(rbac,new Dr(r1, 3));
		rbacAdmin .addDr(rbac,new Dr(r2, 1));
		rbacAdmin .addDr(rbac,new Dr(r3, 3));
		rbacAdmin .addDr(rbac,new Dr(r4, 2));

		Role[] ssodSet = {r1,r2};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssodSet, 1));
		Role[] dsodSet = {r2,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(dsodSet, 1));

		rbacAdmin.assignUser(rbac, u1, r1);
		rbacAdmin.assignUser(rbac, u2, r1);
		rbacAdmin.assignUser(rbac, u4, r1);

		rbacAdmin.assignUser(rbac, u1, r2);
		rbacAdmin.assignUser(rbac, u2, r2);
		rbacAdmin.assignUser(rbac, u5, r2);

		rbacAdmin.assignUser(rbac, u1, r3);
		rbacAdmin.assignUser(rbac, u2, r3);
		rbacAdmin.assignUser(rbac, u4, r3);

		rbacAdmin.assignUser(rbac, u4, r4);

		return rbac;
	}

	public RbacPolicy create_Masood2009P2(){
		RbacPolicy rbac = new RbacPolicy("Masood2009P2");

		//create users
		User u1 = new User("U1");
		User u2 = new User("U2");

		//add users to policy
		rbacAdmin .addUser(rbac,u1);
		rbacAdmin .addUser(rbac,u2);


		rbacAdmin .addDu(rbac,new Du(u1, 2));
		rbacAdmin .addDu(rbac,new Du(u2, 2));

		//create role
		Role r1 = new Role("R1");
		Role r2 = new Role("R2");
		Role r3 = new Role("R3");
		Role r4 = new Role("R4");
		Role r5 = new Role("R5");
		Role r6 = new Role("R6");

		//add role to policy
		rbacAdmin.addRole(rbac,r1);
		rbacAdmin.addRole(rbac,r2);
		rbacAdmin.addRole(rbac,r3);
		rbacAdmin.addRole(rbac,r4);
		rbacAdmin.addRole(rbac,r5);
		rbacAdmin.addRole(rbac,r6);

		//add role constraints to policy
		rbacAdmin .addDr(rbac,new Dr(r1, 1));
		rbacAdmin .addDr(rbac,new Dr(r2, 1));
		rbacAdmin .addDr(rbac,new Dr(r3, 1));
		rbacAdmin .addDr(rbac,new Dr(r4, 1));
		rbacAdmin .addDr(rbac,new Dr(r5, 1));
		rbacAdmin .addDr(rbac,new Dr(r6, 1));

		Role[] ssodSet = {r1,r2};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssodSet, 1));
		Role[] dsodSet = {r2,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(dsodSet, 1));

		rbacAdmin.assignUser(rbac, u1, r1);

		Role[] roles ={r2,r3,r4,r5,r6};
		for (Role r : roles) {
			rbacAdmin.assignUser(rbac, u1, r);
			rbacAdmin.assignUser(rbac, u2, r);
		}

		Role[] s1 = {r1,r2,r3};
		Role[] s2 = {r4,r5};
		Role[] d1 = {r1,r2};
		Role[] d2 = {r2,r3,r4};

		rbac.getSSoDConstraint().add(new SSoDConstraint(s1, 2));
		rbac.getSSoDConstraint().add(new SSoDConstraint(s2, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(d1, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(d2, 2));

		return rbac;
	}

	public RbacPolicy create_user11roles2(){
		RbacPolicy rbac = new RbacPolicy("user11roles2");

		//create users
		for (int i = 0; i <= 11; i++) {
			rbacAdmin .addUser(rbac,new User("U"+i));			
		}

		//create role
		Set<Role> sodSet = new HashSet<Role>();
		for (int i = 0; i <= 2; i++) {
			Role r = new Role("R"+i);
			rbacAdmin .addRole(rbac,r);
			sodSet.add(r);
		}

		for (int i = 0; i <= 4; i++) {
			rbacAdmin .addPermission(rbac,new Permission("P"+i));			
		}


		return rbac;
	}

	public RbacPolicy create_Mondal2009Example1(){
		RbacPolicy rbac = new RbacPolicy("Mondal2009Example1");

		//create users
		for (int i = 0; i <= 16; i++) {
			rbacAdmin .addUser(rbac,new User("U"+i));			
		}

		//create role
		for (int i = 0; i <= 4; i++) {
			rbacAdmin .addRole(rbac,new Role("R"+i));			
		}

		for (int i = 1; i <= 4; i++) {
			rbacAdmin .addPermission(rbac,new Permission("P"+i));			
		}

		rbacAdmin.assignUser(rbac, rbac.getUser().get(6), rbac.getRole().get(0));
		rbacAdmin.assignUser(rbac, rbac.getUser().get(6), rbac.getRole().get(1));

		rbac.getActivationHierarchy().add(new ActivationHierarchy(rbac.getRole().get(1), rbac.getRole().get(4)));
		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(rbac.getRole().get(1), rbac.getRole().get(4)));
		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(rbac.getRole().get(0), rbac.getRole().get(2)));

		Role[] d1 = {rbac.getRole().get(1),rbac.getRole().get(3)};
		rbac.getDSoDConstraint().add(new DSoDConstraint(d1, 1));

		return rbac;
	}

	public RbacPolicy create_SecureBank(){
		RbacPolicy rbac = new RbacPolicy("SecureBank");

		//create users
		User dave = new User("Dave");
		User sarah = new User("Sarah");
		User john = new User("John");
		User mark = new User("Mark");

		//add users to policy
		rbacAdmin .addUser(rbac,dave);
		rbacAdmin .addUser(rbac,sarah);
		rbacAdmin .addUser(rbac,john);
		rbacAdmin .addUser(rbac,mark);

		//create role
		Role r1 = new Role("Teller");
		Role r2 = new Role("LoanOfficer");
		Role r3 = new Role("DayTime System Operator (DSO)");
		Role r4 = new Role("NightTime System Operator (NSO)");
		Role r5 = new Role("System Operator Manager (SOM)");

		//add role to policy
		rbacAdmin.addRole(rbac,r1);
		rbacAdmin.addRole(rbac,r2);
		rbacAdmin.addRole(rbac,r3);
		rbacAdmin.addRole(rbac,r4);
		rbacAdmin.addRole(rbac,r5);

		rbacAdmin.assignUser(rbac, dave,  r1);
		rbacAdmin.assignUser(rbac, sarah, r2);
		rbacAdmin.assignUser(rbac, john,  r3);
		rbacAdmin.assignUser(rbac, john,  r4);
		rbacAdmin.assignUser(rbac, mark,  r5);

		Permission wrtf = new Permission("WRTF: Read and Write Teller Files");
		Permission wrlf = new Permission("WRLF: Read and Write Loan Files");
		Permission wrsof = new Permission("WRSOF: Read and Write System Operator Files");

		rbacAdmin.grantPermission(rbac, wrtf, r1);
		rbacAdmin.grantPermission(rbac, wrlf, r2);
		rbacAdmin.grantPermission(rbac, wrsof, r3);
		rbacAdmin.grantPermission(rbac, wrsof, r4);



		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(r5, r4));
		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(r5, r3));
		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(r5, r2));
		rbac.getInheritanceHierarchy().add(new InheritanceHierarchy(r5, r1));

		//		Role[] s1 = {r3,r4};
		//		rbac.getSsodConstraint().add(new SSoDConstraint(s1, 1));

		Role[] d1 ={r1,r2};
		rbac.getDSoDConstraint().add(new DSoDConstraint(d1, 1));
		//		Role[] d2 ={r3,r3};
		//		rbac.getDsodConstraint().add(new DSoDConstraint(d2, 1));

		return rbac;
	}

	public RbacPolicy create_ProcureToStockV2(){
		RbacPolicy rbac = new RbacPolicy("ProcureToStockV2");

		//create users
		User alice = new User("Alice");
		User bob = new User("Bob");
		User carol = new User("Carol");
		User employee = new User("Employee Name");
		//add to policy
		rbac.getUser().add(alice);
		rbac.getUser().add(carol);
		rbac.getUser().add(bob);
		rbac.getUser().add(employee);

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

		rbacAdmin.assignUser(rbac, alice, r1);
		rbacAdmin.assignUser(rbac, alice, r2);
		rbacAdmin.assignUser(rbac, alice, r3);
		rbacAdmin.assignUser(rbac, alice, r4);
		rbacAdmin.assignUser(rbac, alice, r5);

		rbacAdmin.assignUser(rbac, bob, r1);
		rbacAdmin.assignUser(rbac, bob, r2);
		rbacAdmin.assignUser(rbac, bob, r3);
		rbacAdmin.assignUser(rbac, bob, r5);

		rbacAdmin.assignUser(rbac, carol, r4);
		rbacAdmin.assignUser(rbac, carol, r5);

		rbacAdmin.assignUser(rbac, employee, r1);
		rbacAdmin.assignUser(rbac, employee, r4);

		rbacAdmin.grantPermission(rbac, p1, r1);

		rbacAdmin.grantPermission(rbac, p2, r2);
		rbacAdmin.grantPermission(rbac, p3, r2);

		rbacAdmin.grantPermission(rbac, p4, r3);
		rbacAdmin.grantPermission(rbac, p5, r3);
		rbacAdmin.grantPermission(rbac, p6, r3);
		rbacAdmin.grantPermission(rbac, p7, r3);
		rbacAdmin.grantPermission(rbac, p8, r3);
		rbacAdmin.grantPermission(rbac, p9, r3);

		rbacAdmin.grantPermission(rbac, p10, r4);
		rbacAdmin.grantPermission(rbac, p11, r4);
		rbacAdmin.grantPermission(rbac, p12, r4);

		rbacAdmin.grantPermission(rbac, p13, r5);
		rbacAdmin.grantPermission(rbac, p14, r5);
		rbacAdmin.grantPermission(rbac, p15, r5);

		rbac.getSr().add(new Sr(r5, 1));

		Role[] constR1R2 = {r1,r2};
		Role[] constR1R3 = {r1,r3};
		Role[] constR1R5 = {r1,r5};
		Role[] constR2R3 = {r2,r3};
		Role[] constR3R5 = {r3,r5};
		rbac.getDSoDConstraint().add(new DSoDConstraint(constR1R2, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(constR1R3, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(constR1R5, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(constR2R3, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(constR3R5, 1));


		Role[] constR4R2 = {r4,r2};
		Role[] constR4R3 = {r4,r3};
		Role[] constR4R5 = {r4,r5};
		rbac.getSSoDConstraint().add(new SSoDConstraint(constR4R2, 1));
		rbac.getSSoDConstraint().add(new SSoDConstraint(constR4R3, 1));
		rbac.getSSoDConstraint().add(new SSoDConstraint(constR4R5, 1));

		return rbac;
	}

	public RbacPolicy create_ExperiencePointsv2(){
		RbacPolicy rbac = new RbacPolicy("ExperiencePointsv2");

		//create users
		User u1 = new User("u1");
		User u2 = new User("u2");
		//		//add to policy
		rbac.getUser().add(u1);
		rbac.getUser().add(u2);

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

		rbac.getSr().add(new Sr(admin, 1));

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

		rbacAdmin.grantPermission(rbac, p1, admin);
		rbacAdmin.grantPermission(rbac, p2, admin);
		rbacAdmin.grantPermission(rbac, p3, admin);
		rbacAdmin.grantPermission(rbac, p4, admin);

		rbacAdmin.grantPermission(rbac, p1, bronze);
		rbacAdmin.grantPermission(rbac, p2, bronze);
		rbacAdmin.grantPermission(rbac, p3, bronze);

		rbacAdmin.grantPermission(rbac, p1, silver);
		rbacAdmin.grantPermission(rbac, p2, silver);
		rbacAdmin.grantPermission(rbac, p3, silver);
		rbacAdmin.grantPermission(rbac, p4, silver);

		rbacAdmin.grantPermission(rbac, p1, gold);
		rbacAdmin.grantPermission(rbac, p2, gold);
		rbacAdmin.grantPermission(rbac, p3, gold);
		rbacAdmin.grantPermission(rbac, p4, gold);

		Role[] ssd1 = {admin,gold,bronze,silver};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssd1, 2));
		Role[] ssd2 = {gold,bronze,silver};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssd2, 1));
		Role[] dsd1 = {admin,gold,bronze,silver};
		rbac.getDSoDConstraint().add(new DSoDConstraint(dsd1, 1));

		return rbac;
	}

	public RbacPolicy create_Masood2009P1v2(){
		RbacPolicy rbac = new RbacPolicy("Masood2009P1v2");

		//create users
		User u1 = new User("U1");
		User u2 = new User("U2");
		//		User u3 = new User("U3");
		User u4 = new User("U4");
		//		User u5 = new User("U5");

		//add users to policy
		rbacAdmin .addUser(rbac,u1);
		rbacAdmin .addUser(rbac,u2);
		//		rbacAdmin .addUser(rbac,u3);
		rbacAdmin .addUser(rbac,u4);
		//		rbacAdmin .addUser(rbac,u5);


		rbacAdmin .addDu(rbac,new Du(u1, 2));
		rbacAdmin .addDu(rbac,new Du(u2, 2));
		//		rbacAdmin .addDu(rbac,new Du(u3, 2));
		rbacAdmin .addDu(rbac,new Du(u4, 2));
		//		rbacAdmin .addDu(rbac,new Du(u5, 1));

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
		rbacAdmin .addDr(rbac,new Dr(r1, 3));
		rbacAdmin .addDr(rbac,new Dr(r2, 1));
		rbacAdmin .addDr(rbac,new Dr(r3, 3));
		rbacAdmin .addDr(rbac,new Dr(r4, 2));

		Role[] ssodSet = {r1,r2};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssodSet, 1));
		Role[] ssodSet2 = {r1,r3,r4};
		rbac.getSSoDConstraint().add(new SSoDConstraint(ssodSet2, 1));

		Role[] dsodSet = {r2,r3};
		rbac.getSSoDConstraint().add(new SSoDConstraint(dsodSet, 1));

		rbacAdmin.assignUser(rbac, u1, r1);
		rbacAdmin.assignUser(rbac, u2, r1);
		rbacAdmin.assignUser(rbac, u4, r1);

		rbacAdmin.assignUser(rbac, u1, r2);
		rbacAdmin.assignUser(rbac, u2, r2);
		//		rbacAdmin.assignUser(rbac, u5, r2);

		rbacAdmin.assignUser(rbac, u1, r3);
		rbacAdmin.assignUser(rbac, u2, r3);
		rbacAdmin.assignUser(rbac, u4, r3);

		rbacAdmin.assignUser(rbac, u4, r4);

		return rbac;
	}

	public RbacPolicy create_Masood2009P2v2(){
		RbacPolicy rbac = new RbacPolicy("Masood2009P2v2");

		//create users
		User u1 = new User("U1");
		User u2 = new User("U2");

		//add users to policy
		rbacAdmin .addUser(rbac,u1);
		rbacAdmin .addUser(rbac,u2);


		rbacAdmin .addDu(rbac,new Du(u1, 2));
		rbacAdmin .addSu(rbac,new Su(u2, 1));

		//create role
		Role r1 = new Role("R1");
		Role r2 = new Role("R2");
		Role r3 = new Role("R3");
		Role r4 = new Role("R4");
		Role r5 = new Role("R5");
		//		Role r6 = new Role("R6");

		//add role to policy
		rbacAdmin.addRole(rbac,r1);
		rbacAdmin.addRole(rbac,r2);
		rbacAdmin.addRole(rbac,r3);
		rbacAdmin.addRole(rbac,r4);
		rbacAdmin.addRole(rbac,r5);
		//		rbacAdmin.addRole(rbac,r6);

		//add role constraints to policy
		rbacAdmin .addDr(rbac,new Dr(r1, 1));
		rbacAdmin .addDr(rbac,new Dr(r2, 1));
		rbacAdmin .addDr(rbac,new Dr(r3, 1));
		rbacAdmin .addDr(rbac,new Dr(r4, 1));
		rbacAdmin .addDr(rbac,new Dr(r5, 1));
		//		rbacAdmin .addDr(rbac,new Dr(r6, 1));

		//		Role[] ssodSet = {r1,r2};
		//		rbac.getSSoDConstraint().add(new SSoDConstraint(ssodSet, 1));
		//		Role[] dsodSet = {r2,r3};
		//		rbac.getSSoDConstraint().add(new SSoDConstraint(dsodSet, 1));

		rbacAdmin.assignUser(rbac, u1, r1);

		Role[] roles ={r2,r3,r4,r5
				//				,r6
		};
		for (Role r : roles) {
			rbacAdmin.assignUser(rbac, u1, r);
			rbacAdmin.assignUser(rbac, u2, r);
		}

		Role[] s1 = {r1,r2,r3};
		Role[] s2 = {r4,r5};
		Role[] d1 = {r1,r2};
		Role[] d2 = {r2,r3,r4};

		rbac.getSSoDConstraint().add(new SSoDConstraint(s1, 2));
		rbac.getSSoDConstraint().add(new SSoDConstraint(s2, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(d1, 1));
		rbac.getDSoDConstraint().add(new DSoDConstraint(d2, 2));

		return rbac;
	}

	public RbacPolicy create_userXrolesY(){
		int totUsers = 9;
		int totRoles = 2;
		RbacPolicy rbac = new RbacPolicy("user"+totUsers+"roles"+totRoles);

		//create users
		for (int i = 1; i <= totUsers; i++) {
			rbacAdmin .addUser(rbac,new User("U"+i));			
		}

		//create role
		Set<Role> sodSet = new HashSet<Role>();
		for (int i = 1; i <= totRoles; i++) {
			Role r = new Role("R"+i);
			rbacAdmin .addRole(rbac,r);
			rbac.getSr().add(new Sr(r, 1));
			sodSet.add(r);
		}

		for (int i = 0; i <= 4; i++) {
			rbacAdmin .addPermission(rbac,new Permission("P"+i));			
		}

		rbac.getSSoDConstraint().add(new SSoDConstraint(sodSet, 2));

		return rbac;
	}

	public List<RbacPolicy> getAllPoliciesUnderTest() {
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
		//			policies.add(create_SeniorTraineeDoctor()); //XXX OK!!
		//			policies.add(create_Masood2010Example1()); //XXX OK!!
		//			policies.add(create_ExperiencePointsv2()); //XXX OK!!
		//			policies.add(create_Masood2009P2());
		//			policies.add(create_Masood2009P2v2()); //XXX OK!!
		//			policies.add(create_Masood2009Example1()); //XXX similar to Masood2010Example1

		//		policies.add(create_ProcureToStock());
		//		policies.add(create_ProcureToStockV2());

		//		policies.add(create_Masood2009P1());
		policies.add(create_Masood2009P1v2());

		//		policies.add(create_user11roles2());

		//		policies.add(create_user5roles3());

		//		policies.add(create_Mondal2009Example1());

		//		policies.add(create_SecureBank());

		for (RbacPolicy rbacPol : policies)  for (MutantType mutantType : types) mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));

		for (RbacPolicy rbacPol : mutants) for (MutantType mutantType : types) mutants2nd.addAll(rbacMut.generateMutants(rbacPol, mutantType));

		List<RbacPolicy> mutants1st2nd = new ArrayList<RbacPolicy>();
		mutants1st2nd.addAll(policies);
		//			mutants1st2nd.addAll(mutants);
		//			mutants1st2nd.addAll(mutants2nd);	
		System.out.println("no. 1st order mutants: "+mutants.size());
		System.out.println("no. 2nd order mutants: "+mutants2nd.size());

		return mutants1st2nd;
	}

	public List<RbacPolicy> getSmallPoliciesUnderTest() {
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
		policies.add(create_SeniorTraineeDoctor()); //XXX OK!!
		policies.add(create_Masood2010Example1()); //XXX OK!!

		for (RbacPolicy rbacPol : policies)  for (MutantType mutantType : types) mutants.addAll(rbacMut.generateMutants(rbacPol, mutantType));

		for (RbacPolicy rbacPol : mutants) for (MutantType mutantType : types) mutants2nd.addAll(rbacMut.generateMutants(rbacPol, mutantType));

		List<RbacPolicy> mutants1st2nd = new ArrayList<RbacPolicy>();
		mutants1st2nd.addAll(policies);
		mutants1st2nd.addAll(mutants);
		mutants1st2nd.addAll(mutants2nd);	
		System.out.println("no. policies generated: "+policies.size());
		System.out.println("no. 1 st order mutants: "+mutants.size());
		System.out.println("no. 2 nd order mutants: "+mutants2nd.size());

		return mutants1st2nd;
	}

}
