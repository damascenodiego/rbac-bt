package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestDeassignUR extends RbacRequest{

	public RbacRequestDeassignUR(User u, Role r) {
		super();
		setType(RbacRequest.DEASSIGN_UR);
		setUser(u);
		setRole(r);
	}
}
