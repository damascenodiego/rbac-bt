package com.usp.icmc.labes.rbac.features;


import java.util.HashSet;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacReviewFunctions {

	private static RbacReviewFunctions instance;

	public static RbacReviewFunctions getInstance() {
		if(instance==null){
			instance = new RbacReviewFunctions();
		}
		return instance;
	}

	private RbacReviewFunctions() {}

	public Set<Role> assignedRoles(RbacPolicy policy, User user){
		Set<Role> result = new HashSet<Role>();
		for (UserRoleAssignment el : RbacUtils.getInstance().getUserRoleAssignmentWithUser(policy,user)) {
			result.add(el.getRole());
		}		
		return result;
	}
	public Set<User> assignedUsers(RbacPolicy policy, Role role){
		Set<User> result = new HashSet<User>();
		for (UserRoleAssignment el : RbacUtils.getInstance().getUserRoleAssignmentWithRole(policy,role)) {
			result.add(el.getUser());
		}		
		return result;
	}

}
