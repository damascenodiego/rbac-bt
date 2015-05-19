import java.io.File;
import java.io.FileNotFoundException;

import com.usp.icmc.labes.rbac.model.masood.ansi.Permission;
import com.usp.icmc.labes.rbac.model.masood.ansi.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.masood.ansi.RbacPolicy;
import com.usp.icmc.labes.rbac.model.masood.ansi.Role;
import com.usp.icmc.labes.rbac.model.masood.ansi.User;
import com.usp.icmc.labes.rbac.model.masood.ansi.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;


public class RbacMain {

	public static void main(String[] args) {
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
		ur2.getActiveRoles().add(customer);
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

}
