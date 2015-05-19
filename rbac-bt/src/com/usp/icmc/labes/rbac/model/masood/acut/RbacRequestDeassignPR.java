package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestDeassignPR extends RbacRequest{

	public RbacRequestDeassignPR(String i, Permission p, Role r) {
		setType(i);
		setPermission(p);
		setRole(r);
	}
}
