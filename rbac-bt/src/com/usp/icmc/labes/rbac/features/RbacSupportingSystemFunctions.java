package com.usp.icmc.labes.rbac.features;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

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
		UserRoleActivation ua 		= utils.getUserRoleActivation(policy, user, role);
		boolean userRoleActive 		= (ua!=null);

		boolean nextDuIsValid 		= utils.afterActivateDuIsValid(policy, user);
		boolean nextDrIsValid 		= utils.afterActivateDrIsValid(policy, role);

//		if(		userExists &&
//				roleExists &&
//				userRoleAssigned &&
//				nextDuIsValid &&
//				nextDrIsValid &&
//				!userRoleActive
//				){
//			policy.getUserRoleActivation().add(new UserRoleActivation(user, role));
//			return true;
//		}
//		return false;
//	}
	
//	public boolean addActiveRole(RbacPolicy policy, User user, Role role){
//		boolean userExists 			= utils.userExists(policy, user);
//		boolean roleExists 			= utils.roleExists(policy,role); 
//
		Set<Role> rolesActive = utils.getRolesActivatedByUser(policy, user);
		rolesActive.add(role);
//		boolean userRoleAssigned 	= rolesAssigned.contains(role);
//
//		UserRoleActivation ua 		= utils.getUserRoleActivation(policy, user, role);
//		boolean userRoleActive 		= (ua!=null);
//
//		boolean nextDuIsValid 		= utils.afterActivateDuIsValid(policy, user);
//		boolean nextDrIsValid 		= utils.afterActivateDrIsValid(policy, role);
//
		Map<DSoDConstraint, Set<Role> > totalRolesActive = new HashMap<DSoDConstraint, Set<Role> > ();
		boolean dsdValid = true;
		for (DSoDConstraint dsd : policy.getDsodConstraint()) {
			Set<Role> ac = new HashSet<Role>();
			for (Role r : dsd.getSodSet()) {
				if(rolesActive.contains(r)) ac.add(r);
			}
			totalRolesActive.put(dsd, ac);
			if(dsd.getCardinality()<ac.size()) dsdValid = false;
		}

		
		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				nextDuIsValid &&
				nextDrIsValid &&
				dsdValid &&
				!userRoleActive
				){
			policy.getUserRoleActivation().add(new UserRoleActivation(user, role));
			return true;
		}
		return false;
	}

	public boolean dropActiveRole(RbacPolicy policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 
		
		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned 	= (ur!=null);
		
		UserRoleActivation ua 		= utils.getUserRoleActivation(policy, user, role);
		boolean userRoleActive 		= (ua!=null);

		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				userRoleActive
				){
//			policy.getUserRoleAssignment().remove(ur);
			policy.getUserRoleActivation().remove(ua);
			return true;
		}
		return false;
	}

	public Set<Permission> checkAccess(RbacPolicy pol, User usr, Permission permission){
		Set<Role> rolesOfUsr = utils.getRolesAssignedToUser(pol, usr);
		Set<Permission> prmSet = new HashSet<Permission>();
		for (Role role : rolesOfUsr) {
			for (PermissionRoleAssignment pr : utils.getPermissionRoleAssignmentWithRole(pol, role)) {
				prmSet.add(pr.getPermission());
			}
		}
		return prmSet;
	}

}
