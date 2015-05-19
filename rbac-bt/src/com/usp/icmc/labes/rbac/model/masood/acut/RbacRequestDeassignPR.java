package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestDeassignPR extends RbacRequest{

	public RbacRequestDeassignPR(Permission p, Role r) {
		super();
		setType(RbacRequest.DEASSIGN_PR);
		setPermission(p);
		setRole(r);
	}
}
