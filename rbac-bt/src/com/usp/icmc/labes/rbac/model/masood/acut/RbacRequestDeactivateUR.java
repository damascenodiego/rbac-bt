package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestDeactivateUR extends RbacRequest{

	public RbacRequestDeactivateUR(User u, Role r) {
		super();
		setType(RbacRequest.DEACTIVATE_UR);
		setUser(u);
		setRole(r);
	}
}
