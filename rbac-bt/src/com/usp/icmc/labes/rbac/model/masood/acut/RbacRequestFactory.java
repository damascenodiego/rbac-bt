package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestFactory {

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
		if(type.equals(RbacRequest.ASSIGN_UR)) 		result = new RbacRequestAssignUR(u, r);
		if(type.equals(RbacRequest.DEASSIGN_UR))	result = new RbacRequestDeassignUR(u, r);
		if(type.equals(RbacRequest.ACTIVATE_UR))	result = new RbacRequestActivateUR(u, r);
		if(type.equals(RbacRequest.DEACTIVATE_UR))	result = new RbacRequestDeactivateUR(u, r);
		return result;
	}
	public RbacRequest createRbacRequest(String type, Permission p, Role r){
		RbacRequest result = null;
		if(type.equals(RbacRequest.ASSIGN_PR)) 		result = new RbacRequestAssignPR(p, r);
		if(type.equals(RbacRequest.DEASSIGN_PR))	result = new RbacRequestDeassignPR(p, r);
		return result;
	}
}
