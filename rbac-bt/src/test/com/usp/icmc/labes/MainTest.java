package test.com.usp.icmc.labes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.RoleConstraint;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserConstraint;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class MainTest {

	private RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private RbacUtils rbacUtils = RbacUtils.getInstance();

	RbacPolicy rbac;
	RbacAcut acut;
	User u1,u2;
	Role r1;
	Permission p1,p2;
	UserConstraint u1Card,u2Card;
	RoleConstraint r1Card;

	@Before
	public void setupRbacPolicy() {
		rbac = new RbacPolicy("rbacViaAdminCommand");

		//create users
		u1 = new User("u1");
		u2 = new User("u2");

		//create role
		r1 = new Role("r1");

		//create user cardinality constraints
		u1Card = new UserConstraint(u1, 1,1);
		u2Card = new UserConstraint(u2, 1,1);

		//create role cardinality constraints
		r1Card = new RoleConstraint(r1, 2,1);

		//create permissions
		p1 = new Permission("p1");
		p2 = new Permission("p2");

		acut = new RbacAcut(rbac);
		System.out.println(acut);

		//add users to policy
		//first add OK
		assertTrue(rbacAdmin .addUser(rbac,u1));
		//second add false
		assertFalse(rbacAdmin.addUser(rbac,u1));

		//add user constraints to policy
		assertTrue(rbacAdmin .addUserConstraint(rbac,u1Card));
		assertTrue(rbacAdmin .addUserConstraint(rbac,u2Card));

		//add role constraints to policy
		assertTrue(rbacAdmin .addRoleConstraint(rbac,r1Card));


		//first add OK
		assertTrue(rbacAdmin.addUser(rbac,u2));
		// second add false
		assertFalse(rbacAdmin.addUser(rbac,u2));

		//add role to policy
		//first add ok
		assertTrue(rbacAdmin.addRole(rbac,r1));
		//		second add false
		assertFalse(rbacAdmin.addRole(rbac,r1));

		System.out.println(acut);


		//add permissions
		//first add ok
		assertTrue(rbacAdmin.addPermission(rbac,p1));
		assertTrue(rbacAdmin.addPermission(rbac,p2));
		//second add false
		assertFalse(rbacAdmin.addPermission(rbac,p1));
		assertFalse(rbacAdmin.addPermission(rbac,p2));

		//		for (int i = 2; i < 10; i++) {
		//			//first add ok
		//			assertTrue(rbacAdmin.addRole(rbac,new Role("r"+i,1,1)));
		//		}

		try {
			rbacUtils.WriteRbacPolicyAsXML(rbac, new File("policies/Masood2010Example1.rbac"));
			rbac = rbacUtils.LoadRbacPolicyFromXML(new File("policies/Masood2010Example1.rbac"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Ignore
	@Test
	public void test() {

		System.out.println(acut);

		//create PR relationships
		//first ok
		assertTrue(rbacAdmin.grantPermission(rbac, p1,r1));
		assertTrue(rbacAdmin.grantPermission(rbac, p2,r1));
		//second false
		assertFalse(rbacAdmin.grantPermission(rbac, p1,r1));
		assertFalse(rbacAdmin.grantPermission(rbac, p2,r1));

		System.out.println(acut);

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

		//		u1.setStaticCardinality(0);
		//		assertFalse(rbacSys.addActiveRole(rbac, u1, r1));
		//		assertFalse(rbacAdmin.assignUser(rbac, u1, r1));
		//		assertTrue(rbacAdmin.deassignUser(rbac, u1, r1));
		//		assertFalse(rbacAdmin.deassignUser(rbac, u1, r1));
		//		assertFalse(rbacAdmin.deassignUser(rbac, u1, r1));
		//		assertFalse(rbacAdmin.assignUser(rbac, u1, r1));

		System.out.println(acut);

	}


	@Test
	public void testGenerateRbacFsm() {
		generateRbacFsm(rbac);

	}
	public void generateRbacFsm(RbacPolicy pol){
		//		rbac.getUserRoleAssignment().clear();
		//		List<RbacRequest> input = new ArrayList<RbacRequest>();
		//		for (Role rol: rbac.getRole()) {
		//			for (User usr: rbac.getUser()) {
		//				input.add(new RbacRequestAssignUR(usr, rol));
		//				input.add(new RbacRequestDeassignUR(usr, rol));
		//				input.add(new RbacRequestActivateUR(usr, rol));
		//				input.add(new RbacRequestDeactivateUR(usr, rol));
		//			}
		//			for (Permission prms: rbac.getPermission()) {
		//				input.add(new RbacRequestAssignPR(prms, rol));
		//				input.add(new RbacRequestDeassignPR(prms, rol));
		//
		//			}
		//		}
		//
		//		try {
		//
		//			RbacAcut acut = new RbacAcut(pol);
		//
		//			RbacState 	origin 			= null;
		//			boolean 	out 			= false;
		//			RbacState	 destination 	= null;
		//
		//
		//			FsmModel rbac2fsm = new FsmModel(rbac.getName());
		//
		//			Queue<RbacState> toVisit = new LinkedList<RbacState>();
		//			toVisit.add(acut.getCurrentState());
		//
		//			List<RbacState> visited = new ArrayList<RbacState>();
		//
		//			while (!toVisit.isEmpty()) {
		//				origin = toVisit.remove();
		//				acut.reset(origin);
		//				if(!visited.contains(origin)){
		//					visited.add(origin);
		//					rbac2fsm.addState(new FsmState(origin.getName()));
		//					for (RbacRequest in : input) {
		//						out = acut.request(in);
		//						destination = acut.getCurrentState();
		//						rbac2fsm.addTransition(new FsmTransition(new FsmState(origin.getName()), new FsmState(destination.getName()), in.toString(), (out? "grant" : "deny")));
		//						if(!visited.contains(destination)) 
		//							toVisit.add(destination);
		//						acut.reset(origin);
		//					}
		//				}
		//			}
		//			
		FsmModel rbac2fsm = FsmUtils.getInstance().rbac2Fsm(pol);

		try{

			File example = new File("policies/Masood2010Example1.rbac");
			RbacUtils.getInstance().WriteRbacPolicyAsXML(pol, example);

			File dotFile = new File("policies/Masood2010Example1.dot");
			FsmUtils.getInstance().WriteFsmAsDot(rbac2fsm, dotFile);

			File gmlFile = new File("policies/Masood2010Example1.gml");
			FsmUtils.getInstance().WriteFsmAsGML(rbac2fsm, gmlFile);

			File kissFile = new File("policies/Masood2010Example1.fsm");
			FsmUtils.getInstance().WriteFsmAsKiss(rbac2fsm, kissFile);

			File csvFile = new File("policies/Masood2010Example1.csv");
			FsmUtils.getInstance().WriteFsmAsCsv(rbac2fsm, csvFile);
			//			
			//			PrintWriter pw = new PrintWriter(new FileWriter(new File("fsmTest.txt")));
			//			pw.println("digraph fsm {");
			//			pw.println("}");
			//			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
