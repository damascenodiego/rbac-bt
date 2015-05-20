package test.com.usp.icmc.labes.rbac.features;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class MainTest {

	private RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private RbacUtils rbacUtils = RbacUtils.getInstance();

	RbacPolicy rbac;
	RbacAcut acut;
	User u1,u2;
	Role r1;
	Permission p1,p2;

	@Before
	public void setupRbacPolicy() {
		rbac = new RbacPolicy("rbacViaAdminCommand");

		//create users
		u1 = new User("u1",1,1);
		u2 = new User("u2",1,1);

		//create role
		r1 = new Role("r1",2,1);

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
		rbac.getUserRoleAssignment().clear();
		List<RbacRequest> input = new ArrayList<RbacRequest>();
		for (Role rol: rbac.getRole()) {
			for (User usr: rbac.getUser()) {
				input.add(new RbacRequestAssignUR(usr, rol));
				input.add(new RbacRequestDeassignUR(usr, rol));
				input.add(new RbacRequestActivateUR(usr, rol));
				input.add(new RbacRequestActivateUR(usr, rol));
			}
		}

		Queue<RbacState> toVisit = new LinkedList<RbacState>();

		// origin + input + output + destination
		Set<Object[]>  oiodVisited = new HashSet<Object[]>();

		Set<RbacState> visited = new HashSet<RbacState>();

		RbacAcut acut = new RbacAcut(pol);

		RbacState origin = null;
		RbacRequest in = null;
		boolean out = true;
		RbacState destination = null;

		toVisit.add(acut.getCurrentState());

		String stateName = "";
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(new File("fsmTest.txt")));
			pw.println("digraph fsm {");

			while (!toVisit.isEmpty()) {
				origin = toVisit.remove();
				stateName = origin.toString();
				if(!visited.contains(origin)){
					for (RbacRequest rq : input) {
						in = rq;
						out = acut.request(rq);
						destination = acut.getCurrentState();
						if(!visited.contains(destination)){
							toVisit.add(destination);
						}
						Object [] oiod = new Object[4];
						oiod[0] = origin;
						oiod[1] = in;
						oiod[2] = out;
						oiod[3] = destination;

						if(!oiodVisited.contains(oiod))
						{
							pw.println(stateName+" -> "+destination+"  [ label=\""+in.toString()+"/"+(out ? "granted": "denied")+"\"];");
							oiodVisited.add(oiod);
						}
						acut.reset(origin);
					}
					visited.add(origin);
				}
			}
			pw.println("}");
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
