package com.usp.icmc.labes.rbac.features;

import java.util.List;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class RbacAdministrativeCommands {

	private RbacUtils utils = RbacUtils.getInstance();
	private static RbacAdministrativeCommands instance;

	public static RbacAdministrativeCommands getInstance() {
		if(instance==null){
			instance = new RbacAdministrativeCommands();
		}
		return instance;
	}

	private RbacAdministrativeCommands() {}

	// core rbac
	public boolean addUser(RbacPolicy policy, User user){
		boolean userExists = utils.userExists(policy, user);
		if(!userExists){
			policy.getUser().add(user);
			return true;
		}
		return false;
	}

	public boolean deleteUser(RbacPolicy policy, User user){
		boolean userExists = utils.userExists(policy, user);
		if(userExists){
			policy.getUser().remove(user);
			//TODO remove assignments
			return true;
		}
		return false;
	}
	public boolean addRole(RbacPolicy policy, Role role){
		boolean roleExists = utils.roleExists(policy, role);
		if(!roleExists){
			policy.getRole().add(role);
			return true;
		}
		return false;
	}

	public boolean deleteRole(RbacPolicy policy, Role role){
		boolean roleExists = utils.roleExists(policy, role);
		if(roleExists){
			policy.getRole().remove(role);
			//TODO remove assignments
			return true;
		}
		return false;
	}

	public boolean addPermission(RbacPolicy policy, Permission prms){
		boolean prmsExists = utils.permissionExists(policy, prms);
		if(!prmsExists){
			policy.getPermission().add(prms);
			return true;
		}
		return false;
	}

	public boolean deletePermission(RbacPolicy policy, Permission prms){
		boolean prmsExists = utils.permissionExists(policy, prms);
		if(prmsExists){
			policy.getPermission().remove(prms);
			//TODO remove assignments
			return true;
		}
		return false;
	}


	public boolean assignUser(RbacPolicy policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 

		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned 	= (ur!=null);

		boolean nextSuIsValid 		= utils.afterAssignSuIsValid(policy, user, role);
		boolean nextSrIsValid 		= utils.afterAssignSrIsValid(policy, user, role);

		if(		userExists &&
				roleExists &&
				nextSuIsValid &&
				nextSrIsValid &&
				!userRoleAssigned
				){
			policy.getUserRoleAssignment().add(new UserRoleAssignment(user,role));
			return true;
		}
		return false;

	}
	public boolean deassignUser(RbacPolicy policy, User user, Role role){
		boolean userExists = utils.userExists(policy, user);
		boolean roleExists = utils.roleExists(policy,role); 

		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned 	= (ur!=null);

		UserRoleActivation ua 		= utils.getUserRoleActivation(policy, user, role);
		boolean userRoleActive 		= (ua!=null);

		if(		userExists &&
				roleExists &&
				userRoleAssigned
				){
			policy.getUserRoleAssignment().remove(ur);
			if(userRoleActive) policy.getUserRoleActivation().remove(ua);
			return true;
		}
		return false;

	}

	public boolean grantPermission(RbacPolicy policy, Permission permission, Role role){

		boolean roleExists = utils.roleExists(policy, role);
		boolean permissionExists = utils.permissionExists(policy, permission);
		boolean prmsAssignedToRole = utils.permissionRoleAssignmentExists(policy, permission, role);

		if(		roleExists &&
				permissionExists &&
				!prmsAssignedToRole){
			policy.getPermissionRoleAssignment().add(new PermissionRoleAssignment(permission, role));
			return true;
		}
		return false;
	}

	public boolean revokePermission(RbacPolicy policy, Permission permission, Role role){

		boolean roleExists = utils.roleExists(policy, role);
		boolean permissionExists = utils.permissionExists(policy, permission);
		PermissionRoleAssignment pr = utils.getPermissionRoleAssignment(policy,permission,role);
		boolean prmsAssignedToRole = (pr!=null);

		if(		roleExists &&
				permissionExists &&
				prmsAssignedToRole){
			policy.getPermissionRoleAssignment().remove(pr);
			return true;
		}
		return false;

	}
}
