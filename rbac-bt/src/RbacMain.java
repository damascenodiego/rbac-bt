import java.io.File;
import java.io.FileNotFoundException;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;


public class RbacMain {

	public static void main(String[] args) {
		main1();
		main2();

		File f1 = new File("example1.rbac");
		File f2 = new File("example1_copy.rbac");
		RbacPolicy main1 = RbacUtils.getInstance().LoadRbacPolicyFromXML(f1);
		RbacPolicy main2 = RbacUtils.getInstance().LoadRbacPolicyFromXML(f2);
		if(main1.equals(main2)){
			System.out.println("igual!!! mesmo usando RbacFeatures:D");
		}
	}

	public static void main1() {
		RbacPolicy rbac = new RbacPolicy("example1");
		File f = new File("example1.rbac");

		//create users
		User john = new User("john",1,1);
		User mary = new User("mary",1,1);
		//create role
		Role customer = new Role("customer",2,1);
		//create permissions
		Permission deposit = new Permission("deposit");
		Permission withdraw = new Permission("withdraw");


		//add users to policy
		rbac.getUser().add(john);
		rbac.getUser().add(mary);
		//add role to policy
		rbac.getRole().add(customer);
		//add permissions
		rbac.getPermission().add(deposit);
		rbac.getPermission().add(withdraw);

		//create UR relationships
		UserRoleAssignment ur1 = new UserRoleAssignment(john, customer);
		UserRoleAssignment ur2 = new UserRoleAssignment(mary, customer);
		//create PR relationships
		PermissionRoleAssignment pr1 = new PermissionRoleAssignment(deposit,customer);
		PermissionRoleAssignment pr2 = new PermissionRoleAssignment(withdraw,customer);

		//add UR to policy
		rbac.getUserRoleAssignment().add(ur1);
		rbac.getUserRoleAssignment().add(ur2);
		ur1.getActiveRoles().add(customer);
		//add PR to policy
		rbac.getPermissionRoleAssignment().add(pr1);
		rbac.getPermissionRoleAssignment().add(pr2);

		try {
			RbacUtils.getInstance().WriteRbacPolicyAsXML(rbac, f);
			RbacPolicy pol = RbacUtils.getInstance().LoadRbacPolicyFromXML(f);
			if(pol.equals(rbac)){
				System.out.println("igual!!! :D");
			}
			pol.getUser().get(0).setName("xxx");
			if(!pol.equals(rbac)){
				System.out.println("diferente!!! :D");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main2() {
		RbacPolicy rbac2 = new RbacPolicy("example1");
		File f = new File("example1_copy.rbac");

		//create users
		User john = new User("john",1,1);
		User mary = new User("mary",1,1);
		//create role
		Role customer = new Role("customer",2,1);
		//create permissions
		Permission deposit = new Permission("deposit");
		Permission withdraw = new Permission("withdraw");


		//add users to policy
		RbacAdministrativeCommands.getInstance().addUser(rbac2,john);
		RbacAdministrativeCommands.getInstance().addUser(rbac2,mary);
		//add role to policy
		RbacAdministrativeCommands.getInstance().addRole(rbac2,customer);
		//add permissions
		RbacAdministrativeCommands.getInstance().addPermission(rbac2,deposit);
		RbacAdministrativeCommands.getInstance().addPermission(rbac2,withdraw);

		//create UR relationships
		RbacAdministrativeCommands.getInstance().assignUser(rbac2, john, customer);
		RbacAdministrativeCommands.getInstance().assignUser(rbac2, mary, customer);
		//create PR relationships
		RbacAdministrativeCommands.getInstance().grantPermission(rbac2, deposit,customer);
		RbacAdministrativeCommands.getInstance().grantPermission(rbac2, withdraw,customer);

		//add UR to policy
		System.out.println(RbacSupportingSystemFunctions.getInstance().addActiveRole(rbac2, john, customer));
		System.out.println(RbacSupportingSystemFunctions.getInstance().addActiveRole(rbac2, john, customer));

		try {
			RbacUtils.getInstance().WriteRbacPolicyAsXML(rbac2, f);
			RbacPolicy pol = RbacUtils.getInstance().LoadRbacPolicyFromXML(f);
			if(pol.equals(rbac2)){
				System.out.println("igual!!! :D");
			}
			pol.getUser().get(0).setName("xxx");
			if(!pol.equals(rbac2)){
				System.out.println("diferente!!! :D");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
