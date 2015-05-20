package com.usp.icmc.labes.rbac.acut;

import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class RbacRequestActivateUR extends RbacRequest{
	
	public RbacRequestActivateUR(User u, Role r) {
		super();
		setType(RbacRequest.ACTIVATE_UR);
		setUser(u);
		setRole(r);
	}

}
