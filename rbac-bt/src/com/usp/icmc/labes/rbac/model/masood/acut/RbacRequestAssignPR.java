package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestAssignPR extends RbacRequest{

	public RbacRequestAssignPR(String i, Permission p, Role r) {
		setType(i);
		setPermission(p);
		setRole(r);
	}
}
