package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public class RbacRequestActivateUR extends RbacRequest{
	
	public RbacRequestActivateUR(User u, Role r) {
		super();
		setType(RbacRequest.ACTIVATE_UR);
		setUser(u);
		setRole(r);
	}

}
