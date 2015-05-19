package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestAssignPR extends RbacRequest{

	public RbacRequestAssignPR(Permission p, Role r) {
		super();
		setType(RbacRequest.ASSIGN_PR);
		setPermission(p);
		setRole(r);
	}
}
