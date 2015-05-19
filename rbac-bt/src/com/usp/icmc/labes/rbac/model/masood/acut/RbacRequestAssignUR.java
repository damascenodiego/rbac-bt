package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestAssignUR extends RbacRequest{

	public RbacRequestAssignUR(String i, User u, Role r) {
		setType(i);
		setUser(u);
		setRole(r);
	}
}
