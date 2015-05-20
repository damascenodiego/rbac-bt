package com.usp.icmc.labes.rbac.acut;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.Role;

public class RbacRequestDeassignPR extends RbacRequest{

	public RbacRequestDeassignPR(Permission p, Role r) {
		super();
		setType(RbacRequest.DEASSIGN_PR);
		setPermission(p);
		setRole(r);
	}
}
