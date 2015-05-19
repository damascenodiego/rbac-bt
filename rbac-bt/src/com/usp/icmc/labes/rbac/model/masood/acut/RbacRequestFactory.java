package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestFactory {

	public static final String ASSIGN_UR    	= "ASSIGN_UR";
	public static final String DEASSIGN_UR    	= "DEASSIGN_UR";

	public static final String ACTIVATE_UR    	= "ACTIVATE_UR";
	public static final String DEACTIVATE_UR    = "DEACTIVATE_UR";

	public static final String ASSIGN_PR    	= "ASSIGN_PR";
	public static final String DEASSIGN_PR    	= "DEASSIGN_PR";

	private static RbacRequestFactory instance ; 


	private RbacRequestFactory() { }

	public static RbacRequestFactory getInstance() {
		if(instance == null){
			instance = new RbacRequestFactory();
		}
		return instance;
	}
	public RbacRequest createRbacRequest(String type, User u, Role r){
		RbacRequest result = null;
		if(type.equals(ASSIGN_UR)) 		result = new RbacRequestAssignUR(type, u, r);
		if(type.equals(DEASSIGN_UR))	result = new RbacRequestDeassignUR(type, u, r);
		if(type.equals(ACTIVATE_UR))	result = new RbacRequestActivateUR(type, u, r);
		if(type.equals(DEACTIVATE_UR))	result = new RbacRequestDeactivateUR(type, u, r);
		return result;
	}
	public RbacRequest createRbacRequest(String type, Permission p, Role r){
		RbacRequest result = null;
		if(type.equals(ASSIGN_PR)) 		result = new RbacRequestAssignPR(type, p, r);
		if(type.equals(DEASSIGN_PR))	result = new RbacRequestDeassignPR(type, p, r);
		return result;
	}
}
