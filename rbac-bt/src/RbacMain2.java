//import java.io.File;
//import java.io.FileNotFoundException;
//
//import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
//import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
//import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
//import com.usp.icmc.labes.rbac.model.Permission;
//import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
//import com.usp.icmc.labes.rbac.model.RbacPolicy;
//import com.usp.icmc.labes.rbac.model.Role;
//import com.usp.icmc.labes.rbac.model.User;
//import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
//import com.usp.icmc.labes.rbac.utils.RbacUtils;
//
//
//public class RbacMain2 {
//
//	public static void main(String[] args) {
//		main1();
//		main2();
//
//		File f1 = new File("exampleInheritance.rbac");
//		File f2 = new File("exampleInheritance_copy.rbac");
//		
//		RbacPolicy pol1 = RbacUtils.getInstance().LoadRbacPolicyFromXML(f1);
//		RbacPolicy pol2 = RbacUtils.getInstance().LoadRbacPolicyFromXML(f2);
//		if(pol1.equals(pol2)){
//			System.out.println("igual!!! :D");
//		}
//
//		
//	}
//
//	public static void main1() {
//		RbacPolicy rbac = new RbacPolicy("example1");
//		File f = new File("exampleInheritance.rbac");
//
//		//create users
//		User john = new User("john",2,2);
//		User mary = new User("mary",2,2);
//		//create role
//		Role revi = new Role("reviewer",2,1);
//		Role clone = new Role("clone",2,1);
//		Role auth = new Role("author",2,1);
//		//create permissions
//
//		//add users to policy
//		rbac.getUser().add(john);
//		rbac.getUser().add(mary);
//		//add role to policy
//		rbac.getRole().add(revi);
//		rbac.getRole().add(auth);
//		rbac.getRole().add(clone);
//
//		//create UR relationships
//		UserRoleAssignment ur1 = new UserRoleAssignment(john, revi);
//		UserRoleAssignment ur2 = new UserRoleAssignment(mary, auth);
//
//		ActivationHierarchy reviCanActivAuth = new ActivationHierarchy(revi, auth);
//		//activation hierarchy
//		rbac.getActivationHierarchy().add(reviCanActivAuth);
//
//		//add UR to policy
//		rbac.getUserRoleAssignment().add(ur1);
//		rbac.getUserRoleAssignment().add(ur2);
//
//		try {
//			RbacUtils.getInstance().WriteRbacPolicyAsXML(rbac, f);
//			RbacPolicy pol = RbacUtils.getInstance().LoadRbacPolicyFromXML(f);
//			if(pol.equals(rbac)){
//				System.out.println("igual!!! :D");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public static void main2() {
//		RbacPolicy rbac = new RbacPolicy("example1");
//		File f = new File("exampleInheritance_copy.rbac");
//
//
//		//create users
//		User john = new User("john",2,2);
//		User mary = new User("mary",2,2);
//		//create role
//		Role revi = new Role("reviewer",2,1);
//		Role clone = new Role("clone",2,1);
//		Role auth = new Role("author",2,1);
//
//		//add users to policy
//		RbacAdministrativeCommands.getInstance().addUser(rbac,john);
//		RbacAdministrativeCommands.getInstance().addUser(rbac,mary);
//		//add role to policy
//		RbacAdministrativeCommands.getInstance().addRole(rbac,revi);
//		RbacAdministrativeCommands.getInstance().addRole(rbac,auth);
//		RbacAdministrativeCommands.getInstance().addRole(rbac,clone);
//
//		//create UR relationships
//		RbacAdministrativeCommands.getInstance().assignUser(rbac, john, revi);
//		RbacAdministrativeCommands.getInstance().assignUser(rbac, mary, auth);
//
//		try {
//			RbacUtils.getInstance().WriteRbacPolicyAsXML(rbac, f);
//			RbacPolicy pol = RbacUtils.getInstance().LoadRbacPolicyFromXML(f);
//			if(pol.equals(rbac)){
//				System.out.println("igual!!! :D");
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
