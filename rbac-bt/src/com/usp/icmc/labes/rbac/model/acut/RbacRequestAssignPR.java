package com.usp.icmc.labes.rbac.model.acut;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.Role;

public class RbacRequestAssignPR extends RbacRequest{

	public RbacRequestAssignPR(Permission p, Role r) {
		super();
		setType(RbacRequest.ASSIGN_PR);
		setPermission(p);
		setRole(r);
	}
}
