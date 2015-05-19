package com.usp.icmc.labes.rbac.features;


import com.usp.icmc.labes.rbac.model.masood.ansi.*;
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

	//	public void createSession(RbacPolicy policy, User user, String sessionName){
	//	XXX not yet necessary 
	//	}

	//	public void deleteSession(RbacPolicy policy, User user, Session session){
	//	XXX not yet necessary
	//	}

	public boolean addActiveRole(RbacPolicy policy, User user, 
			//Session session, // XXX not yet necessary
			Role role){
		boolean userExists = utils.userExists(policy, user);
		boolean roleExists = utils.roleExists(policy,role); 
		
		UserRoleAssignment ur 		= utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned	= (ur!=null);
		boolean userRoleActive 		= utils.userRoleActive(policy, user, role);

		boolean nextDuIsValid 	= utils.afterActivateDuIsValid(policy, user, role);
		boolean nextDrIsValid 	= utils.afterActivateDrIsValid(policy, user, role);

		boolean nextSSoDIsValid 	= utils.afterAssignSsodIsValid(policy, user, role);
		boolean nextDSoDIsValid 	= utils.afterAssignDsodIsValid(policy, user, role);
		
		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				nextDuIsValid &&
				nextDrIsValid &&
				nextSSoDIsValid && //TODO test SSoD constraints
				nextDSoDIsValid && //TODO test DSoD constraints
				!userRoleActive
				){
			ur.getActiveRoles().add(role);
			return true;
		}
		return false;
	}

	public boolean dropActiveRole(RbacPolicy policy, User user, 
			//Session session, // XXX not yet necessary 
			Role role){
		boolean userExists = utils.userExists(policy, user);
		boolean roleExists = utils.roleExists(policy,role); 
		UserRoleAssignment ur = utils.getUserRoleAssignment(policy, user, role);
		boolean userRoleAssigned = (ur!=null);
		boolean userRoleActive = utils.userRoleActive(policy, user, role);

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

	//	public boolean checkAccess(RbacPolicy policy, Session session, Permission permission){
	// XXX not yet necessary
	//		return true;
	//	}

}
