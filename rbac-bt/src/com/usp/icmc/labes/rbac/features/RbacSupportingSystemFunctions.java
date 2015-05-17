package com.usp.icmc.labes.rbac.features;


import com.usp.icmc.labes.rbac.model.*;

public class RbacSupportingSystemFunctions {

	private RbacSupportingSystemFunctions instance;

	public RbacSupportingSystemFunctions getInstance() {
		if(instance==null){
			instance = new RbacSupportingSystemFunctions();
		}
		return instance;
	}

	private RbacSupportingSystemFunctions() {}

	public void createSession(RbacPolicy policy, User user, String sessionName){

	}
	public void deleteSession(RbacPolicy policy, User user, Session session){

	}
	public void addActiveRole(RbacPolicy policy, User user, Session session, Role role){

	}

	public void deleteActiveRole(RbacPolicy policy, User user, Session session, Role role){

	}
	public boolean checkAccess(RbacPolicy policy, Session session, Permission permission){
		return true;
	}

}
