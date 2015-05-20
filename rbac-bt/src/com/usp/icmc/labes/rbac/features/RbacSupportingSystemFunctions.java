package com.usp.icmc.labes.rbac.features;


import java.util.HashSet;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class RbacSupportingSystemFunctions {

	private RbacUtils utils = RbacUtils.getInstance();
	private static RbacSupportingSystemFunctions instance;

	public static RbacSupportingSystemFunctions getInstance() {
		if(instance==null){
			instance = new RbacSupportingSystemFunctions();
		}
		return instance;
	}

	private RbacSupportingSystemFunctions() {}

	public boolean addActiveRole(RbacPolicy policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 

		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned	= (ur!=null);
		boolean userRoleActive 		= utils.isRoleActiveByUser(policy, role, user);

		boolean nextDuIsValid 		= utils.afterActivateDuIsValid(policy, user, role);
		boolean nextDrIsValid 		= utils.afterActivateDrIsValid(policy, user, role);

		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				nextDuIsValid &&
				nextDrIsValid &&
				!userRoleActive
				){
			ur.getActiveRoles().add(role);
			return true;
		}
		return false;
	}

	public boolean dropActiveRole(RbacPolicy policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 
		
		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned 	= (ur!=null);
		
		boolean userRoleActive 		= utils.isRoleActiveByUser(policy, role, user);

		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				userRoleActive
				){
			ur.getActiveRoles().remove(role);
			return true;
		}
		return false;
	}

	public Set<Permission> checkAccess(RbacPolicy pol, User usr, Permission permission){
		Set<Role> rolesOfUsr = utils.getRolesAssignedToUser(pol, usr);
		Set<Permission> prmSet = new HashSet<Permission>();
		for (Role role : rolesOfUsr) {
			for (PermissionRoleAssignment pr : utils.getPermissionRoleAssignmentsWithRole(pol, role)) {
				prmSet.add(pr.getPermission());
			}
		}
		return prmSet;
	}

}
