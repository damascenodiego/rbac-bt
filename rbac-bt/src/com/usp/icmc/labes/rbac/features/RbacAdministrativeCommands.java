package com.usp.icmc.labes.rbac.features;

import java.util.Set;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;
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
		//TODO to be implemented later	
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
		//TODO to be implemented later	
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
		//TODO to be implemented later
		return false;
	}


	public boolean assignUser(RbacPolicy policy, User user, Role role){
		boolean userExists 			= utils.userExists(policy, user);
		boolean roleExists 			= utils.roleExists(policy,role); 
		
		boolean userAssignedtoRole 	= utils.userRoleAssignmentExists(policy, user, role);
		
		boolean nextSuIsValid 		= utils.afterAssignSuIsValid(policy, user, role);
		boolean nextSrIsValid 		= utils.afterAssignSrIsValid(policy, user, role);

		boolean nextSSoDIsValid 	= utils.afterAssignSsodIsValid(policy, user, role);
		
		if(		userExists &&
				roleExists &&
				nextSuIsValid &&
				nextSrIsValid &&
				nextSSoDIsValid &&
				!userAssignedtoRole
				){
			policy.getUserRoleAssignment().add(new UserRoleAssignment(user,role));
			return true;
		}
		return false;

	}
	public boolean deassignUser(RbacPolicy policy, User user, Role role){
		boolean userExists = utils.userExists(policy, user);
		boolean roleExists = utils.roleExists(policy,role); 
		UserRoleAssignment ur = utils.getUserRoleAssignment(policy, user, role);
		boolean userAssignedtoRole = (ur!=null);

		if(		userExists &&
				roleExists &&
				userAssignedtoRole
				){
			policy.getUserRoleAssignment().remove(ur);
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
	
	public boolean revokePermission(RbacPolicy policy, Role role, Permission permission){
		
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
	// hierarchical rbac
	public void addInheritance(RbacPolicy policy, Role r_asc, Role r_desc){

	}
	public void deleteInheritance(RbacPolicy policy, Role r_asc, Role r_desc){

	}
	public void addAscendant(RbacPolicy policy, Role r_asc, Role r_desc){

	}
	public void addDescendant(RbacPolicy policy, Role r_asc, Role r_desc){

	}
	// static SoD
	public void createSsodSet(RbacPolicy policy, String name , Set<Role> set, int set_card){

	}
	public void deleteSsodSet(RbacPolicy policy, SSoDConstraint ssod){

	}
	public void addSsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role){

	}
	public void setSsodSetCardinality(RbacPolicy policy, SSoDConstraint ssod, int set_card){

	}
	public void deleteSsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role){

	}
	// dynamic SoD
	public void createDsodSet(RbacPolicy policy, String name , Set<Role> set, int set_card){

	}
	public void deleteDsodSet(RbacPolicy policy, SSoDConstraint ssod){

	}
	public void addDsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role){

	}
	public void setDsodSetCardinality(RbacPolicy policy, SSoDConstraint ssod, int set_card){

	}
	public void deleteDsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role){

	}
}
