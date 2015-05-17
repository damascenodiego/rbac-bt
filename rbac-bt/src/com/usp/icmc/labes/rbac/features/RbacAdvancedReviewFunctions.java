package com.usp.icmc.labes.rbac.features;


import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.rbac.model.*;

public class RbacAdvancedReviewFunctions {

	private RbacAdvancedReviewFunctions instance;

	public RbacAdvancedReviewFunctions getInstance() {
		if(instance==null){
			instance = new RbacAdvancedReviewFunctions();
		}
		return instance;
	}

	private RbacAdvancedReviewFunctions() {}

	public List<Permission> rolePermissions(RbacPolicy policy, Role role){
		List<Permission> result = new ArrayList<Permission>();
		
		return result;

	}
	public List<Permission> userPermissions(RbacPolicy policy, User role){
		List<Permission> result = new ArrayList<Permission>();
		
		return result;
	}
	public List<Role> sessionRoles(RbacPolicy policy, Session session){
		List<Role> result = new ArrayList<Role>();
		
		return result;
	}
	public List<Permission> sessionPermissions(RbacPolicy policy, Session session){
		List<Permission> result = new ArrayList<Permission>();
		
		return result;
	}

}
