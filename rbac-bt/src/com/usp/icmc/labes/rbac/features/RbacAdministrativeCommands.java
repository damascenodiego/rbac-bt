package com.usp.icmc.labes.rbac.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacMutableElement;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.RbacTuple;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;
import com.usp.icmc.labes.utils.RbacUtils.RbacFaultType;

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
	public boolean addUser(RbacTuple policy, User user){
		boolean userExists = utils.userExists(policy, user);
		if(!userExists){
			policy.getUser().add(user);
			return true;
		}
		return false;
	}

	public boolean deleteUser(RbacTuple policy, User user){
		boolean userExists = utils.userExists(policy, user);
		if(userExists){
			policy.getUser().remove(user);
			policy.getUserRoleAssignment().removeAll(utils.getUserRoleAssignmentWithUser(policy, user));
			policy.getUserRoleActivation().removeAll(utils.getUserRoleActivationWithUser(policy, user));
			return true;
		}
		return false;
	}
	public boolean addRole(RbacTuple policy, Role role){
		boolean roleExists = utils.roleExists(policy, role);
		if(!roleExists){
			policy.getRole().add(role);
			return true;
		}
		return false;
	}

	public boolean deleteRole(RbacTuple policy, Role role){
		boolean roleExists = utils.roleExists(policy, role);
		if(roleExists){
			policy.getRole().remove(role);
			policy.getUserRoleAssignment().removeAll(utils.getUserRoleAssignmentWithRole(policy, role));
			policy.getUserRoleActivation().removeAll(utils.getUserRoleActivationWithRole(policy, role));
			return true;
		}
		return false;
	}

	public boolean addPermission(RbacTuple policy, Permission prms){
		boolean prmsExists = utils.permissionExists(policy, prms);
		if(!prmsExists){
			policy.getPermission().add(prms);
			return true;
		}
		return false;
	}

	public boolean deletePermission(RbacTuple policy, Permission prms){
		boolean prmsExists = utils.permissionExists(policy, prms);
		if(prmsExists){
			policy.getPermission().remove(prms);
			policy.getPermissionRoleAssignment().removeAll(utils.getPermissionRoleAssignmentWithPermission(policy, prms));
			return true;
		}
		return false;
	}


	public boolean assignUser(RbacTuple policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 

		Set<Role> rolesAssigned = utils.getRolesAssignedToUser(policy, user);
		boolean userRoleAssigned 	= rolesAssigned.contains(role);
		
		rolesAssigned.add(role);
		
		boolean nextSuIsValid 		= utils.afterAssignSuIsValid(policy, user);
		boolean nextSrIsValid 		= utils.afterAssignSrIsValid(policy, role);

		Map<SSoDConstraint, Set<Role> > totalRolesAssigned = new HashMap<SSoDConstraint, Set<Role> > ();
		boolean ssdValid = true;
		for (SSoDConstraint ssd : policy.getSSoDConstraint()) {
			Set<Role> as = new HashSet<Role>();
			for (Role r : ssd.getSodSet()) {
				if(rolesAssigned.contains(r)) as.add(r);
			}
			totalRolesAssigned.put(ssd, as);
			if(ssd.getCardinality()<as.size()) ssdValid = false;
		}
		
		if(		userExists &&
				roleExists &&
				nextSuIsValid &&
				nextSrIsValid &&
				ssdValid &&
				!userRoleAssigned
				){
			policy.getUserRoleAssignment().add(new UserRoleAssignment(user,role));
			return true;
		}else{
			RbacPolicy p = null;
			if(policy instanceof RbacPolicy) {
				p = (RbacPolicy) policy;
			}else if(policy instanceof RbacAcut) {
				p = ((RbacAcut) policy).getPolicy();
			}
			p.getProperties().clear();
			RbacMutableElement reason = null;
			RbacFaultType faultType = null;
			if(!nextSuIsValid){
				faultType = RbacFaultType.SuFailed;
				reason = utils.getSu(policy, user);
				utils.failedDueTo(p,faultType,reason);
			}
			if(!nextSrIsValid){
				faultType = RbacFaultType.SrFailed;
				reason = utils.getSr(policy, role);
				utils.failedDueTo(p,faultType,reason);
			}
			if(!ssdValid){
				faultType = RbacFaultType.SsodFailed;
				List<SSoDConstraint> reasonList = utils.getSSoD(policy, role);
				for (RbacMutableElement rbacMutableElement : reasonList) {
					utils.failedDueTo(p,faultType,rbacMutableElement);
				}
			}
			
			
		}
		return false;

	}
	public boolean deassignUser(RbacTuple policy, User user, Role role){
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

	public boolean grantPermission(RbacTuple policy, Permission permission, Role role){

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

	public boolean revokePermission(RbacTuple policy, Permission permission, Role role){

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

	public boolean addSu(RbacTuple rbac, Su constr) {
		if(!rbac.getSu().contains(constr)){
			rbac.getSu().add(constr);
			return true;
		}
		return false;
	}

	public boolean addSr(RbacTuple rbac, Sr constr) {
		if(!rbac.getSr().contains(constr)){
			rbac.getSr().add(constr);
			return true;
		}
		return false;
	}

	public boolean addDu(RbacTuple rbac, Du constr) {
		if(!rbac.getDu().contains(constr)){
			rbac.getDu().add(constr);
			return true;
		}
		return false;
	}

	public boolean addDr(RbacTuple rbac, Dr constr) {
		if(!rbac.getDr().contains(constr)){
			rbac.getDr().add(constr);
			return true;
		}
		return false;
	}
}
