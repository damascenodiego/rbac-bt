package com.usp.icmc.labes.rbac.features;

import java.util.HashSet;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacAdvancedReviewFunctions {

	static private RbacAdvancedReviewFunctions instance;
	public static RbacAdvancedReviewFunctions getInstance() {
		if(instance==null){
			instance = new RbacAdvancedReviewFunctions();
		}
		return instance;
	}

	private RbacUtils utils = RbacUtils.getInstance();

	private RbacAdvancedReviewFunctions() {}

	public Set<Permission> rolePermissions(RbacPolicy policy, Role role){
		Set<Permission> result = new HashSet<Permission>();
		for (PermissionRoleAssignment el : utils.getPermissionRoleAssignmentWithRole(policy,role)) {
			result.add(el.getPermission());
		}		
		return result;

	}
	public Set<Permission> userPermissions(RbacPolicy policy, User usr){
		Set<Permission> result = new HashSet<Permission>();
		for (Role rol: utils.getRolesAssignedToUser(policy, usr)) {
			for (PermissionRoleAssignment el : utils.getPermissionRoleAssignmentWithRole(policy,rol)) {
				result.add(el.getPermission());
			}
		}
		return result;
	}

}
