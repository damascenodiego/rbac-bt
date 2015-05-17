package com.usp.icmc.labes.rbac.features;


import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.rbac.model.*;

public class RbacReviewFunctions {

	private RbacReviewFunctions instance;

	public RbacReviewFunctions getInstance() {
		if(instance==null){
			instance = new RbacReviewFunctions();
		}
		return instance;
	}

	private RbacReviewFunctions() {}

	/////////////////////////////////////
	// core rbac
	/////////////////////////////////////
	public List<User> assignedUsers(RbacPolicy policy, Role role){
		List<User> result = new ArrayList<User>();
		
		return result;

	}
	public List<Role> assignedRoles(RbacPolicy policy, User role){
		List<Role> result = new ArrayList<Role>();
		
		return result;
	}

	/////////////////////////////////////
	// hierarchical rbac
	/////////////////////////////////////
	public List<User> authorizedUsers(RbacPolicy policy, Role role){
		List<User> result = new ArrayList<User>();
		
		return result;
	}
	public List<Role> authorizedRoles(RbacPolicy policy, User role){
		List<Role> result = new ArrayList<Role>();
		
		return result;
	}
	
	/////////////////////////////////////
	// static SoD
	/////////////////////////////////////
	public List<SSoDConstraint> ssodRoleSets(RbacPolicy policy){
		List<SSoDConstraint> result = new ArrayList<SSoDConstraint>();
		
		return result;
	}
	
	public List<Role> ssodRoleSetRoles(RbacPolicy policy, SSoDConstraint constraint){
		List<Role> result = new ArrayList<Role>();
		
		return result;
	}
	public int ssodRoleSetCardinality(RbacPolicy policy, SSoDConstraint constraint){
		return 0;
	}
	
	/////////////////////////////////////
	// dynamic SoD
	/////////////////////////////////////
	public List<DSoDConstraint> dsodRoleSets(RbacPolicy policy){
		List<DSoDConstraint> result = new ArrayList<DSoDConstraint>();
		
		return result;
	}
	
	public List<Role> dsodRoleSetRoles(RbacPolicy policy, DSoDConstraint constraint){
		List<Role> result = new ArrayList<Role>();
		
		return result;
	}
	public int dsodRoleSetCardinality(RbacPolicy policy, DSoDConstraint constraint){
		return 0;
	}
}
