package com.usp.icmc.labes.rbac.model.acut;

import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class RbacRequestAssignUR extends RbacRequest{

	public RbacRequestAssignUR(User u, Role r) {
		super();
		setType(RbacRequest.ASSIGN_UR);
		setUser(u);
		setRole(r);
	}
}
