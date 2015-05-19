package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestAssignUR extends RbacRequest{

	public RbacRequestAssignUR(User u, Role r) {
		super();
		setType(RbacRequest.ASSIGN_UR);
		setUser(u);
		setRole(r);
	}
}
