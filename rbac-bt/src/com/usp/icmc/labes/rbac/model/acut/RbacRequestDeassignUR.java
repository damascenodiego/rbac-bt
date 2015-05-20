package com.usp.icmc.labes.rbac.model.acut;

import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class RbacRequestDeassignUR extends RbacRequest{

	public RbacRequestDeassignUR(User u, Role r) {
		super();
		setType(RbacRequest.DEASSIGN_UR);
		setUser(u);
		setRole(r);
	}
}
