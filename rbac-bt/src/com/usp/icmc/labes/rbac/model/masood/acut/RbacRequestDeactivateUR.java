package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestDeactivateUR extends RbacRequest{

	public RbacRequestDeactivateUR(String i, User u, Role r) {
		setType(i);
		setUser(u);
		setRole(r);
	}
}
