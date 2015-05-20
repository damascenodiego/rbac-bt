package com.usp.icmc.labes.rbac.model.acut;

import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class RbacRequestDeactivateUR extends RbacRequest{

	public RbacRequestDeactivateUR(User u, Role r) {
		super();
		setType(RbacRequest.DEACTIVATE_UR);
		setUser(u);
		setRole(r);
	}
}
